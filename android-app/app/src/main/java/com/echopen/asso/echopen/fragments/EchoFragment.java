package com.echopen.asso.echopen.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.echopen.asso.echopen.EchOpenApplication;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EchoFragment extends Fragment {

    private boolean burstFireFolderCreated = false;
    private Date burstFireFolderDate;
    private Activity activity;
    Button btn_capture;

    public EchoFragment() {
        // Required empty public constructor
        // Init probe
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

         // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_echo, container, false);

        btn_capture = (Button) v.findViewById(R.id.button_capture);

        initProbe();

        // Start home
        startHome();

        return v;
    }

    /**
     * Start home features
     */
    private void startHome()
    {
        //Remove notification bar
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Buttons initialisation
        // Single short click triggers a single picture
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSingle();
            }
        });

        // Long click + Release
        btn_capture.setOnTouchListener(new View.OnTouchListener() {
            private android.os.Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new android.os.Handler();
                        mHandler.postDelayed(mAction, 500);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        burstFireFolderCreated = false;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    if(!burstFireFolderCreated) {
                        // The actual date for the file name
                        Date now = new Date();
                        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                        burstFireFolderCreated = true;
                        burstFireFolderDate = now;

                        createBurstFireFolder();
                    } else {
                        takeBurstFire();
                    }
                    mHandler.postDelayed(this, 500);
                }
            };
        });
    }

    /**
     * Start linking to the probe
     */
    private void initProbe() {
        EchOpenApplication app = (EchOpenApplication) activity.getApplication();
        EchographyImageStreamingService stream = app.getEchographyImageStreamingService();

        EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(stream, new EchographyImageVisualisationContract.View(){
            @Override
            public void refreshImage(final Bitmap iBitmap){
                try{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView echoImage = (ImageView) activity.findViewById(R.id.echography);

                            Display display = activity.getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int width = size.x;
                            int height = size.y;


                            echoImage.setImageBitmap(iBitmap);
                            echoImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            System.out.println("Bitmap received");
                            Log.d("Debug",iBitmap.getHeight() + "");
                            Log.d("Debug",iBitmap.getWidth() + "");
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void setPresenter(EchographyImageVisualisationContract.Presenter presenter){

            }
        });
        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode("10.6.200.128", 7538);

        stream.connect(mode,activity);
        presenter.start();
    }

    /**
     * Take pictures in single
     */
    public void takeSingle() {
        takeSingleScreenshot();
    }

    /**
     * This function is triggered when user clicks on capture button
     * And takes a screenshot
     */
    public void takeSingleScreenshot() {

        // The actual date for the file name
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // Check if echopen storage folder is created
            File echopenFolder = new File(Environment.getExternalStorageDirectory() + "/Echopen");
            if(!(echopenFolder.exists() && echopenFolder.isDirectory())) {
                boolean success = echopenFolder.mkdir();

                if(success) {
                    Log.d("Folder created : ", "OK");
                } else {
                    Log.d("Folder create : ", "ERROR");
                }
            }

            // Storage path and file name
            String storagePath = Environment.getExternalStorageDirectory().toString() + "/Echopen/" + "echography - " + now + ".jpg";

            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            // Creates file
            File imageFile = new File(storagePath);

            // Image treatements
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * Take pictures in burst fire mode
     */
    public void takeBurstFire(){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String storagePath = Environment.getExternalStorageDirectory().toString() + "/Echopen/BurstFire-" + burstFireFolderDate + "/" + now + ".jpg";
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            // Creates file
            File imageFile = new File(storagePath);

            // Image treatements
            FileOutputStream output = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output);
            output.flush();
            output.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * Create burst fire folder
     */
    public void createBurstFireFolder() {
        // The actual date for the file name
        File burstfireFolder = new File(Environment.getExternalStorageDirectory() + "/Echopen/BurstFire-" + burstFireFolderDate);

        if (!(burstfireFolder.exists() && burstfireFolder.isDirectory())) {
            boolean success = burstfireFolder.mkdir();
        } else {
            Log.d("Burst fire folder : ", "already exists");
        }
    }

    /**
     * Open screenshots
     * @param imageFile
     */
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}

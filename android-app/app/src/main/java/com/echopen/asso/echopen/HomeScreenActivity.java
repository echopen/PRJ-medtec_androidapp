package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

// Probe import
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HomeScreenActivity extends Activity{

    private float x1;
    private float x2;

    private float y1;
    private float y2;

    private boolean burstFireFolderCreated = false;

    private Date burstFireFolderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // Init probe
        initProbe();

        // Start home
        startHome();
    }

    /**
     * Start home features
     */
    private void startHome()
    {
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Buttons initialisation
        final Button btn_capture = (Button) findViewById(R.id.button_capture);

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
        EchOpenApplication app = (EchOpenApplication) getApplication();
        EchographyImageStreamingService stream = app.getEchographyImageStreamingService();

        EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(stream, new EchographyImageVisualisationContract.View(){
            @Override
            public void refreshImage(final Bitmap iBitmap){
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView echoImage = (ImageView) findViewById(R.id.echography);

                            Display display = getWindowManager().getDefaultDisplay();
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

        stream.connect(mode,this);
        presenter.start();
    }

    /**
     * Swipe motion event
     * @param touchevent
     * @return
     */
    public boolean onTouchEvent(MotionEvent touchevent) {

        if (touchevent.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("move", "move");
        }
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if ((x1 < x2) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "RIGHT");
                }
                if ((x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                }
                if ((y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                }
                if ((y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                }
            }
        }
        return false;
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

            View v1 = getWindow().getDecorView().getRootView();
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
            View v1 = getWindow().getDecorView().getRootView();
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

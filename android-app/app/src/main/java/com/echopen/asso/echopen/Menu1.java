package com.echopen.asso.echopen;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 05/07/2017.
 */

//public class Menu1 extends Fragment {
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //returning our layout file
//        //change R.layout.yourlayoutfilename for each of your fragments
//        return inflater.inflate(R.layout.fragment_menu_1, container, false);
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
//    }
//}


/**
 * A simple {@link Fragment} subclass.
 */
public  class Menu1 extends Fragment implements AbstractActionActivity, EchographyImageVisualisationContract.View {

    private View takePicture;
    private View imageViewBitmap;
    private ImageView goToGallery;

    /* integer constant that switch whether the photo or the video is on */
    private int display;
    /* class that deals with the view of MainActivity */
    //private MainActionController mainActionController;
    public GestureDetector gesture;
    /* Ruler enables get the overview of body scales */
    public RulerView rulerView;
    /* Setter for rulerView */
    public void setRulerView(RulerView rulerView) {
        this.rulerView = rulerView;
    }
    /* constant setting the process via local, UDP or TCP */
    public static boolean LOCAL_ACQUISITION = false;

    public static boolean TCP_ACQUISITION = true;

    public static boolean UDP_ACQUISITION = false;

    private EchographyImageStreamingService mEchographyImageStreamingService;
    private RenderingContextController mRenderingContextController;

    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;

    public Menu1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                /* loading config constants in singleton Class */
        Config.getInstance(getActivity());
        Config.singletonConfig.getWidth();
        /* aiming to synchronize views : when operator drags lines to measure distance between points,
         the measure is synchronized with a TextView */
        Synchronizer.getInstance(getActivity());

        mEchographyImageStreamingService = ((EchOpenApplication) getActivity().getApplication() ).getEchographyImageStreamingService();
        mRenderingContextController = mEchographyImageStreamingService.getRenderingContextController();

        mEchographyImageVisualisationPresenter = new EchographyImageVisualisationPresenter(mEchographyImageStreamingService, this);
        this.setPresenter(mEchographyImageVisualisationPresenter);

        final View rootView = inflater.inflate(R.layout.fragment_menu_1, container, false);

        ImageView organ = (ImageView) rootView.findViewById(R.id.organ);
        organ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "click on imageview");

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.organ_modal, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        init(inflater, container, rootView);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "take pictures");

                saveImage();
            }
        });

        goToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "take pictures");

                displayGallery(rootView);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


    private void displayGallery(View rootView) {

        //initializing the fragment object which is selected
        Fragment fragment = new GalleryFragment();

        //replacing the fragment
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    /**
     * @param inflater
     * @param container
     * @param rootView
     */
    public void init(LayoutInflater inflater, ViewGroup container, View rootView) {
        imageViewBitmap = rootView.findViewById(R.id.echo);
        takePicture = rootView.findViewById(R.id.btnCapture);
        goToGallery = (ImageView) rootView.findViewById(R.id.gallery);
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * save image in gallery
     */
    public void saveImage() {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if (!file.exists() && !file.mkdirs()) {
            Toast.makeText(getActivity(), "Can't create directory for image", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss", Locale.FRANCE);
        String date = simpleDateFormat.format(new Date());
        String name = "Echo" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);

        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(imageViewBitmap, imageViewBitmap.getWidth(), imageViewBitmap.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            Toast.makeText(getActivity(), "Save image success", Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshGallery(new_file);
    }

    /**
     * Refresh gallery
     * @param file
     */
    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        getActivity().sendBroadcast(intent);
    }

    /**
     * get directory file
     * @return
     */
    private File getDisc() {

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image");
    }

    @Override
    public void onResume(){
        super.onResume();
        mEchographyImageVisualisationPresenter.start();
        EchographyImageStreamingTCPMode tcpMode = new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);
        mEchographyImageStreamingService.connect(tcpMode, getActivity());
    }

    /**
     * @param item, MenuItem instance
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    /**
     * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
     * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.”,
     * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     *
     * @param requestCode, integer argument that identifies your request
     * @param resultCode, to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
     * @param data,       Intent instance
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView echoImage = (ImageView) getView().findViewById(R.id.echo);
                    echoImage.setImageBitmap(iBitmap);
                    echoImage.setColorFilter(Config.colorMatrixColorFilter);
                    Timer.logResult("Display Bitmap");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter iPresenter) {
        mEchographyImageVisualisationPresenter = iPresenter;
    }

    @Override
    public void initActionController() {

    }
}
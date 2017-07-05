package com.echopen.asso.echopen;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;

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

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;


/**
 * A simple {@link Fragment} subclass.
 */
public  class Menu1 extends Fragment implements AbstractActionActivity, EchographyImageVisualisationContract.View {


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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_1, container, false);
    }


    @Override
    public void onResume(){
        super.onResume();
        mEchographyImageVisualisationPresenter.start();
        EchographyImageStreamingTCPMode tcpMode = new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);
        mEchographyImageStreamingService.connect(tcpMode, getActivity());
    }

//    @Override
//    public void initActionController() {
//        Menu1 activity = this;
//        mainActionController = new MainActionController(activity);
//    }

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
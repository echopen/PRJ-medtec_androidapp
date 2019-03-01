package com.echopen.asso.echopen.echography_image_visualisation;

import android.app.Activity;
import android.util.Log;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingNotification;
import com.echopen.asso.echopen.model.EchopenImage;
import com.echopen.asso.echopen.model.EchopenImageSequence;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotification;
import com.echopen.asso.echopen.probe_communication.notifications.WifiState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class presenter used to display real time image streaming to user
 */
public class EchographyImageVisualisationPresenter implements EchographyImageVisualisationContract.Presenter {

    private final EchographyImageVisualisationContract.View mView; /* view from MVP design */

    private ArrayList<EchopenImage> mRecordedImages;
    private Boolean mIsRecording;
    private Activity mCurrentContext;

    /**
     * @brief constructor
     *
     * @param iView view from MVP architecture design
     */
    public EchographyImageVisualisationPresenter(EchographyImageVisualisationContract.View iView, Activity iCurrentContext){
        mView = iView;
        mView.setPresenter(this);

        mIsRecording = false;
        mRecordedImages = new ArrayList<>();

        mCurrentContext = iCurrentContext;
    }

    public void finalize() throws Throwable {
        EventBus.getDefault().unregister(this);
        super.finalize();
    }


    @Override
    public void start() {
        EventBus.getDefault().register(this);

    }

    @Override
    public void startRecording() {
        mIsRecording = true;
    }

    @Override
    public void endRecording() {
        mIsRecording = false;
        mRecordedImages.clear();
    }


    @Override
    public void previewImage() {
        if(mRecordedImages == null || mRecordedImages.size() < 1){
            return;
        }

        //TODO: replace MainActivity direct cast
        ((MainActivity) mCurrentContext).goToImagePreview(mRecordedImages.get(0));
    }

    @Override
    public void previewSequence() {
        if(mRecordedImages == null || mRecordedImages.size() < 1){
            return;
        }


        Log.d("previewImage", "preview size " + mRecordedImages.size());
        //TODO: replace MainActivity direct cast

        EchopenImageSequence lSequence = new EchopenImageSequence();

        for(EchopenImage lImage :mRecordedImages){
            lSequence.addImage(lImage);
        }

        ((MainActivity) mCurrentContext).goToSequencePreview(lSequence);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ProbeCommunicationWifiNotification iWifiCommunicationNotification) {
        if(iWifiCommunicationNotification.getState() == WifiState.WIFI_ENABLED){
            mView.displayWifiProgress(mCurrentContext.getResources().getString(R.string.wifi_enabled));
        }
        else if(iWifiCommunicationNotification.getState() == WifiState.WIFI_ENABLED_ERROR){
            mView.displayWifiError(mCurrentContext.getResources().getString(R.string.wifi_enabled_error));
        }
        else if(iWifiCommunicationNotification.getState() == WifiState.WIFI_CONNECTED){
            mView.displayWifiProgress(mCurrentContext.getResources().getString(R.string.wifi_probe_connected));
        }
        else if(iWifiCommunicationNotification.getState() == WifiState.WIFI_CONNECTED_ERROR){
            mView.displayWifiError(mCurrentContext.getResources().getString(R.string.wifi_probe_connected_error));
        }
        else if(iWifiCommunicationNotification.getState() == WifiState.WIFI_START_SCANNING) {
            mView.closeWifiProgress();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEchographyImageStreamingNotification(EchographyImageStreamingNotification iEchographyImageStreamingNotification) {
        if(mIsRecording){
            mRecordedImages.add(new EchopenImage(iEchographyImageStreamingNotification.getImage()));
        }

        mView.refreshImage(iEchographyImageStreamingNotification.getImage());
    }

}

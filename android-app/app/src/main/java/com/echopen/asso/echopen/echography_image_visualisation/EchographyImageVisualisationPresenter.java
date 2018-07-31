package com.echopen.asso.echopen.echography_image_visualisation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingNotification;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingObserver;
import com.echopen.asso.echopen.model.EchopenImage;
import com.echopen.asso.echopen.model.EchopenImageSequence;
import com.echopen.asso.echopen.utils.Timer;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class presenter used to display real time image streaming to user
 */
public class EchographyImageVisualisationPresenter extends EchographyImageStreamingObserver implements EchographyImageVisualisationContract.Presenter {

    private final EchographyImageVisualisationContract.View mView; /* view from MVP design */

    private EchographyImageStreamingService mEchographyImageStreamingService; /* image streaming service */

    private ArrayList<EchopenImage> mRecordedImages;
    private Boolean mIsRecording;
    private Activity mCurrentContext;

    /**
     * @brief constructor
     *
     * @param iEchographyImageStreamingService image streaming service
     * @param iView view from MVP architecture design
     */
    public EchographyImageVisualisationPresenter(EchographyImageStreamingService iEchographyImageStreamingService, EchographyImageVisualisationContract.View iView, Activity iCurrentContext){
        mEchographyImageStreamingService = iEchographyImageStreamingService;

        mView = iView;
        mView.setPresenter(this);

        mIsRecording = false;
        mRecordedImages = new ArrayList<>();

        mCurrentContext = iCurrentContext;
    }


    @Override
    public void start() {
        listenEchographyImageStreaming();
    }

    @Override
    public void listenEchographyImageStreaming() {
        mEchographyImageStreamingService.addObserver(this);
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


    @Override
    public void onEchographyImageStreamingNotification(final EchographyImageStreamingNotification iEchographyImageStreamingNotification)
    {
        if(mIsRecording){
            mRecordedImages.add(new EchopenImage(iEchographyImageStreamingNotification.getImage()));
        }

        mView.refreshImage(iEchographyImageStreamingNotification.getImage());
    }
}

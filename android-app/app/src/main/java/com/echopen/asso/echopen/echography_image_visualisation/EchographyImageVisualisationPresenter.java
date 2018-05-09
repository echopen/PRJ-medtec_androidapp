package com.echopen.asso.echopen.echography_image_visualisation;

import android.graphics.Bitmap;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingNotification;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingObserver;
import com.echopen.asso.echopen.utils.Timer;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class presenter used to display real time image streaming to user
 */
public class EchographyImageVisualisationPresenter extends EchographyImageStreamingObserver implements EchographyImageVisualisationContract.Presenter {

    private final EchographyImageVisualisationContract.View mView; /* view from MVP design */

    private EchographyImageStreamingService mEchographyImageStreamingService; /* image streaming service */

    private Boolean mIsFrozen;

    /**
     * @brief constructor
     *
     * @param iEchographyImageStreamingService image streaming service
     * @param iView view from MVP architecture design
     */
    public EchographyImageVisualisationPresenter(EchographyImageStreamingService iEchographyImageStreamingService, EchographyImageVisualisationContract.View iView){
        mEchographyImageStreamingService = iEchographyImageStreamingService;

        mView = iView;
        mView.setPresenter(this);

        mIsFrozen = false;
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
    public void toggleFreeze() {
        if(mIsFrozen){
            unfreeze();
        }
        else{
            freeze();
        }
    }

    private void freeze(){
        mIsFrozen = true;
        mView.displayFreezeButton();
    }
    private void unfreeze() {
        mIsFrozen = false;
        mView.displayUnfreezeButton();
    }

    @Override
    public void onEchographyImageStreamingNotification(final EchographyImageStreamingNotification iEchographyImageStreamingNotification)
    {
        if(mIsFrozen){
            return;
        }

        mView.refreshImage(iEchographyImageStreamingNotification.getImage());
    }

    public void captureAction(Bitmap imageCaptured)
    {
     }
    public void captureSequenceAction()
    {
        //show loop sequence action on myView.fragment
    }

}

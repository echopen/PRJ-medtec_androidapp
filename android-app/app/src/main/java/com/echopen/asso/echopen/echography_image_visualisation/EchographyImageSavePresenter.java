package com.echopen.asso.echopen.echography_image_visualisation;

import android.app.Activity;
import android.util.Log;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.model.EchopenImage;
import com.echopen.asso.echopen.model.EchopenImageSequence;

public class EchographyImageSavePresenter implements EchographyImageSaveContract.Presenter {

    public static final String TAG = EchographyImageSavePresenter.class.getSimpleName();
    private EchographyImageSaveContract.View mView;
    private Activity mCurrentContext;

    public EchographyImageSavePresenter(EchographyImageSaveContract.View iView, Activity iCurrentContext){
        mView = iView;
        mView.setPresenter(this);
        mCurrentContext = iCurrentContext;
    }

    @Override
    public void cancelPreview() {
        //TODO: remove direct cast to MainActivity - replace by event triggering
        ((MainActivity) mCurrentContext).goToImageStreaming();
    }

    @Override
    public void saveImage(EchopenImage iImage) {
        Log.d(TAG, "Save Image");

        //TODO: remove direct cast to MainActivity - replace by event triggering
        ((MainActivity) mCurrentContext).goToImageStreaming();
    }

    @Override
    public void saveSequence(EchopenImageSequence iSequence) {
        Log.d(TAG, "Save Sequence");

        //TODO: remove direct cast to MainActivity - replace by event triggering
        ((MainActivity) mCurrentContext).goToImageStreaming();
    }

    @Override
    public void start() {

    }
}

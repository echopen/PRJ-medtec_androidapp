package com.echopen.asso.echopen.echography_image_visualisation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.util.Log;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.model.EchopenImage;
import com.echopen.asso.echopen.model.EchopenImageSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EchographySequenceSavePresenter implements EchographySequenceSaveContract.Presenter {

    public static final String TAG = EchographySequenceSavePresenter.class.getSimpleName();
    private EchographySequenceSaveContract.View mView;
    private Activity mCurrentContext;

    private EchopenImageSequence mSequence;
    private Map<SequenceOption.Speed, Long> mSequenceSpeedOptions;
    private SequenceOption.Speed mCurrentSequenceSpeed;
    private ScheduledExecutorService mExecutorService;

    public EchographySequenceSavePresenter(EchographySequenceSaveContract.View iView, Activity iCurrentContext, EchopenImageSequence iSequence){
        mView = iView;
        mView.setPresenter(this);
        mCurrentContext = iCurrentContext;

        mExecutorService = Executors.newScheduledThreadPool(1);
        mCurrentSequenceSpeed = SequenceOption.Speed.UNKNOWN;
        mSequence = iSequence;

        mSequenceSpeedOptions = new HashMap();

        mSequenceSpeedOptions.put(SequenceOption.Speed.VERY_SLOW, SequenceOption.SpeedDisplayIntervalVerySlow);
        mSequenceSpeedOptions.put(SequenceOption.Speed.SLOW, SequenceOption.SpeedDisplayIntervalSlow);
        mSequenceSpeedOptions.put(SequenceOption.Speed.REGULAR, SequenceOption.SpeedDisplayIntervalRegular);


    }

    @Override
    public void cancelPreview() {
        //TODO: remove direct cast to MainActivity - replace by event triggering
        ((MainActivity) mCurrentContext).goToImageStreaming();
    }

    @Override
    public void saveSequence() {
        Log.d(TAG, "Save Sequence");

        //TODO: remove direct cast to MainActivity - replace by event triggering
        ((MainActivity) mCurrentContext).goToImageStreaming();
    }

    @Override
    public void changeSequenceSpeed(SequenceOption.Speed iSpeed) {
        if(mCurrentSequenceSpeed == iSpeed || mSequence == null){
            return;
        }

        mCurrentSequenceSpeed = iSpeed;

        if(!mExecutorService.isShutdown()) {
            mExecutorService.shutdownNow();
        }

        mExecutorService = Executors.newScheduledThreadPool(1);
        mExecutorService.scheduleAtFixedRate(
            new Runnable() {
                private int mCurrentImageIndex = 0;

                @Override
                public void run() {
                    if(mCurrentImageIndex + 1 >= mSequence.getSequenceSize()){
                        mCurrentImageIndex = 0;
                    }

                    mCurrentContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap lImageBitmap = mSequence.getImage(mCurrentImageIndex).getBitmap();
                            mView.refreshImage(lImageBitmap);
                        }
                    });

                    mCurrentImageIndex++;
                }
            },0, (long)getDisplayInterval(iSpeed), TimeUnit.MILLISECONDS);

    }

    @Override
    public void stopSequence() {
        mExecutorService.shutdownNow();
    }

    private Long getDisplayInterval(SequenceOption.Speed iSpeed){
        return mSequenceSpeedOptions.get(iSpeed);
    }

    @Override
    public void start() {
        changeSequenceSpeed(SequenceOption.Speed.REGULAR);
    }
}

package com.echopen.asso.echopen;

import android.app.Application;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.model.Data.ProbeCinematicProvider;
import com.echopen.asso.echopen.probe_communication.ProbeCommunicationService;
import com.echopen.asso.echopen.ui.RenderingContextController;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class echOpen application
 */
public class EchOpenApplication extends Application {

    private final String TAG = this.getClass().getSimpleName();

    private EchographyImageStreamingService mEchographyImageStreamingService; /* image streaming service */
    private ProbeCinematicProvider mProbeCinematicProvider;
    private RenderingContextController mRenderingContextController;
    private ProbeCommunicationService mProbeCommunicationService;

    @Override
    public void onCreate() {
        super.onCreate();

        mRenderingContextController = new RenderingContextController();
        mProbeCinematicProvider = new ProbeCinematicProvider();
        mEchographyImageStreamingService = new EchographyImageStreamingService(mRenderingContextController, mProbeCinematicProvider);
        /*TODO: implement a real Android Service */
        mProbeCommunicationService = new ProbeCommunicationService(getApplicationContext(), mEchographyImageStreamingService);
    }

    /**
     * @brief getter - image streaming service
     *
     * @return image streaming service
     */
    public EchographyImageStreamingService getEchographyImageStreamingService(){
        return mEchographyImageStreamingService;
    }

    public ProbeCommunicationService getProbeCommunicationService(){
        return mProbeCommunicationService;
    }
}

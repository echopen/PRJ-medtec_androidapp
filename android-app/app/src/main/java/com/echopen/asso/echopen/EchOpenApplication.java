package com.echopen.asso.echopen;

import android.app.Application;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.ui.RenderingContextController;

/**
 * Created by lecoucl on 05/06/17.
 */
public class EchOpenApplication extends Application {

    EchographyImageStreamingService mEchographyImageStreaming;

    @Override
    public void onCreate() {
        super.onCreate();

        mEchographyImageStreaming = new EchographyImageStreamingService(new RenderingContextController());

    }

    public EchographyImageStreamingService getEchographyImageStreaming(){
        return mEchographyImageStreaming;
    };
}

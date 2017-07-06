package com.echopen.asso.echopen;

import android.app.Application;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.ui.RenderingContextController;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class echOpen application
 */
public class EchOpenApplication extends Application {

    private final String TAG = this.getClass().getSimpleName();

    private EchographyImageStreamingService mEchographyImageStreaming; /* image streaming service */

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/roboto_medium.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()

        );
        mEchographyImageStreaming = new EchographyImageStreamingService(new RenderingContextController());
    }

    /**
     * @brief getter - image streaming service
     *
     * @return image streaming service
     */
    public EchographyImageStreamingService getEchographyImageStreamingService(){
        return mEchographyImageStreaming;
    }
}

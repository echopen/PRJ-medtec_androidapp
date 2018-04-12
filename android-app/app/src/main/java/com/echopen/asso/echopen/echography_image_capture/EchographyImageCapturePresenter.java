package com.echopen.asso.echopen.echography_image_capture;

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
public class EchographyImageCapturePresenter implements EchographyImageCaptureContract.Presenter {

   // private final EchographyImageCaptureContract.View mView; /* view from MVP design */



    /**
     * @brief constructor
     *
     * @param iEchographyImageStreamingService image streaming service
     * @param iView view from MVP architecture design
     */
    public EchographyImageCapturePresenter(Bitmap imageCaptured){



    }

    @Override
    public void start() {

    }


}

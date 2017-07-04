package com.echopen.asso.echopen.echography_image_streaming.notifications;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class observer interface to handle image streaming notification
 */
public class EchographyImageStreamingObserver implements Observer {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void update(Observable o, Object arg) {
        // filter only image streaming notification events
        if(arg instanceof EchographyImageStreamingNotification){
            this.onEchographyImageStreamingNotification((EchographyImageStreamingNotification) arg);
        }
    }

    /**
     * @brief callback triggered on image streaming notification
     *
     * @param iEchographyImageStreamingNotification notification event
     */
    public void onEchographyImageStreamingNotification(final EchographyImageStreamingNotification iEchographyImageStreamingNotification){}

}

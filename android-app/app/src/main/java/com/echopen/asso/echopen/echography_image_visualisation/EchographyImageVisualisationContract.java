package com.echopen.asso.echopen.echography_image_visualisation;

import android.graphics.Bitmap;

import com.echopen.asso.echopen.utils.BasePresenter;
import com.echopen.asso.echopen.utils.BaseView;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class contract for EchographyImageVisualisation
 */
public class EchographyImageVisualisationContract {

    public interface View extends BaseView<Presenter> {

        /**
         * @brief refresh in realtime main view
         *
         * @param iBitmap bitmap to be displayed
         */
        void refreshImage(final Bitmap iBitmap);

    }


    public interface Presenter extends BasePresenter {

        /**
         * @brief listen to image streaming notification event
         */
        void listenEchographyImageStreaming();

    }
}

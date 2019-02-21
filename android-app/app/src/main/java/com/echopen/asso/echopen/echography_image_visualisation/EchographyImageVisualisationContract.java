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

        /**
         *
         * @param iMessage message to be displayed
         */
        void displayWifiProgress(final String iMessage);

        /**
         *
         * @param iErrorMessage error to be displayed
         */
        void displayWifiError(final String iErrorMessage);

        /**
         *
         *
         */
        void closeWifiProgress();
    }


    public interface Presenter extends BasePresenter {

        void startRecording();

        void endRecording();

        /**
         * @brief preview a single image
         */
        void previewImage();

        /**
         * @brief preview a sequence
         */

        void previewSequence();

    }
}

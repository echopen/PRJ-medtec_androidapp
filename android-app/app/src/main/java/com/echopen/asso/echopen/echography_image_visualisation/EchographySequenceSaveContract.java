package com.echopen.asso.echopen.echography_image_visualisation;

import android.graphics.Bitmap;

import com.echopen.asso.echopen.model.EchopenImage;
import com.echopen.asso.echopen.model.EchopenImageSequence;
import com.echopen.asso.echopen.utils.BasePresenter;
import com.echopen.asso.echopen.utils.BaseView;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class contract for EchographyImageSaveContract
 */
public class EchographySequenceSaveContract {

    public interface View extends BaseView<Presenter> {
        /**
         * @brief refresh currently displayed image
         */

        void refreshImage(Bitmap lImageBitmap);
    }


    public interface Presenter extends BasePresenter {

        /**
         * @brief cancel image / sequence preview
         */
        void cancelPreview();

        /**
         * @brief save currently reviewed sequence
         */

        void saveSequence();

        /**
         * @brief change sequence display speed
         *
         * @param iSpeed selected display speed
         */

        void changeSequenceSpeed(SequenceOption.Speed iSpeed);

        /**
         * @brief shutdown sequence display
         */

        void stopSequence();
    }
}

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
public class EchographyImageSaveContract {

    public interface View extends BaseView<Presenter> {

    }


    public interface Presenter extends BasePresenter {

        /**
         * @brief cancel image / sequence preview
         */
        void cancelPreview();

        /**
         * @brief save an image
         */
        void saveImage(EchopenImage iImage);

        /**
         * @brief save an image sequence
         */

        void saveSequence(EchopenImageSequence iSequence);

    }
}

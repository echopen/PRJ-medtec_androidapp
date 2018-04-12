package com.echopen.asso.echopen.echography_image_capture;

import android.graphics.Bitmap;

import com.echopen.asso.echopen.utils.BasePresenter;
import com.echopen.asso.echopen.utils.BaseView;

/**
 * Created by lecoucl on 06/06/17.
 *
 * @class contract for EchographyImageCapture
 */
public class EchographyImageCaptureContract {

    public interface View extends BaseView<Presenter> {

    }


    public interface Presenter extends BasePresenter {

    }
}

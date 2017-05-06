package com.echopen.asso.echopen.model.Data;

import android.app.Activity;

import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;

/**
 * This is no best practice related to mocking constructor : this is a factory related to test ability.
 * PowerMockito seems not to work properly on Android. At least, I (mehdi benchoufi) was not able to manage it to work
 */
public class BitmapDisplayerFactory {

    public BitmapDisplayerFactory() {
    }

    /**
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     * @param ip, ip to get the incoming UDP data
     * @param port, port on which listening the incoming UDP data
     */
    public BitmapDisplayer populateBitmap(Activity activity, MainActionController mainActionController, RenderingContextController iRenderingContextController, String ip, int port) {
        return new BitmapDisplayer(activity, mainActionController, iRenderingContextController, ip, port);
    }
}

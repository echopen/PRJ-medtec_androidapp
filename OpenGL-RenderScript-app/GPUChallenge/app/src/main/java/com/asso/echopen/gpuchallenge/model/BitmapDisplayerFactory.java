package com.asso.echopen.gpuchallenge.model;

import android.app.Activity;

import com.asso.echopen.gpuchallenge.ui.MainActionController;

/**
 * This is no best practice related to mocking constructor : this is a factory related to test ability.
 * PowerMockito seems not to work properly on Android. At least, I (mehdi benchoufi) was not able to manage it to work
 */
public class BitmapDisplayerFactory {

    public BitmapDisplayerFactory() {
    }

    /**
     * This aims at populating bitmap when the data is fetched form a pared file
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     */
    public BitmapDisplayer populateBitmap(Activity activity, MainActionController mainActionController) {
        return new BitmapDisplayer(activity, mainActionController);
    }
    /**
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     * @param ip, ip to get the incoming UDP data
     * @param port, port on which listening the incoming UDP data
     */
    public BitmapDisplayer populateBitmap(Activity activity, MainActionController mainActionController, String ip, int port) {
        return new BitmapDisplayer(activity, mainActionController, ip, port);
    }
}

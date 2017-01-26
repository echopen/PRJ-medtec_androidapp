package com.asso.echopen.gpuchallenge.model;

import android.app.Activity;

import com.asso.echopen.gpuchallenge.ui.MainActionController;

import java.io.IOException;
import java.io.InputStream;

/**
 * This Class inherits form Displayer class. The latter connects with the UI of the MainActivity
 * handled by the MainActionController class.
 *
 * Specifically
 *
 * todo: implement a code pattern protocol-agnostic.
 */
public class BitmapDisplayer extends com.asso.echopen.gpuchallenge.model.Displayer {

    /* The MainActivity variable */
    private final Activity activity;

    /* The ip to get the incoming UDP data */
    private String ip;

    /* The port on which listening the incoming UDP data */
    private int port;


    /**
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     */
    public BitmapDisplayer(Activity activity, MainActionController mainActionController) {
        super(activity, mainActionController);
        this.activity = activity;
    }

    /**
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     * @param ip, ip to get the incoming UDP data
     * @param port, port on which listening the incoming UDP data
     */
    public BitmapDisplayer(Activity activity, MainActionController mainActionController, String ip, int port) {
        super(activity, mainActionController);
        this.ip = ip;
        this.port = port;
        this.activity = activity;
    }

    public void readDataFromFile(final InputStream is) throws IOException {
        new FileTask(activity, mainActionController, scanConversion, is).execute();
    }
}
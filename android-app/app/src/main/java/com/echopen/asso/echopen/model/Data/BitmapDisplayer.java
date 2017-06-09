package com.echopen.asso.echopen.model.Data;

import android.app.Activity;

import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;

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
public class BitmapDisplayer extends Displayer {

    /* The MainActivity variable */
    private final Activity activity;

    /* The ip to get the incoming UDP data */
    private final String ip;

    /* The port on which listening the incoming UDP data */
    private final int port;

    /**
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     * @param ip, ip to get the incoming UDP data
     * @param port, port on which listening the incoming UDP data
     */
    public BitmapDisplayer(Activity activity, MainActionController mainActionController, RenderingContextController iRenderingContextController, String ip, int port) {
        super(activity, mainActionController, iRenderingContextController);
        this.ip = ip;
        this.port = port;
        this.activity = activity;
    }

    /**
     * This method does  many things
     *  - listening to the incoming data on $ip:$port
     *  - gives the data to the ScanConversion class in order to get the scan converted data
     *  - transforms the scan converted data into an image via EchoIntImage class
     *  - creates the bitmap from some instance of EchoIntImage
     *  - refreshes the UI with the data in a new thread
     *  This is done via an AsyncTask because you can't play with UDP in the main thread
     * @throws IOException
     */

    public void readDataFromUDP() throws IOException {
        new ProcessUPDTask(activity, mRenderingContextController, port).execute();
    }

    public void readDataFromFile(final InputStream is) throws IOException {
        new FileTask(activity, mainActionController, scanConversion, is, mRenderingContextController).execute();
    }
}
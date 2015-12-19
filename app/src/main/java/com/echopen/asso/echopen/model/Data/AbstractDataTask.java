package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.echopen.asso.echopen.model.EchoImage.EchoIntImage;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * Created by loic on 19/12/15.
 */
abstract public class AbstractDataTask extends AsyncTask<Void, Void, Void> {

    protected Activity activity;

    protected ScanConversion scanconversion;

    protected MainActionController mainActionController;

    public AbstractDataTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion) throws IOException {
        this.scanconversion = scanConversion;
        this.activity = activity;
        this.mainActionController = mainActionController;
    }

    protected void refreshUI(ScanConversion scnConv) {
        int[] scannedArray = scnConv.getDataFromInterpolation();
        EchoIntImage echoImage = new EchoIntImage(scannedArray);

        final Bitmap bitmap = echoImage.createImage();
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActionController.displayMainFrame(bitmap);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

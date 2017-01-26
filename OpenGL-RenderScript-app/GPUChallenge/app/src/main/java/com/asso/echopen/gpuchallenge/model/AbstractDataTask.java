package com.asso.echopen.gpuchallenge.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.asso.echopen.gpuchallenge.preproc.ScanConversion;
import com.asso.echopen.gpuchallenge.ui.MainActionController;
import com.asso.echopen.gpuchallenge.utils.Constants;

import java.io.IOException;

/**
 * Core class of data collecting routes. Whether the protocol is chosen to be TCP, UDP or fetching data from local,
 * the dedicated classes inherits from @this
 */
abstract public class AbstractDataTask extends AsyncTask<Void, Void, Void> {

    protected Activity activity;

    protected ScanConversion scanconversion;

    protected MainActionController mainActionController;

    private static int[] scanned_array;

    public static int[] getScanned_array() {
        return scanned_array;
    }

    public AbstractDataTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion) {
        this.scanconversion = scanConversion;
        this.activity = activity;
        this.mainActionController = mainActionController;
    }

    protected void refreshUI(ScanConversion scanconversion) throws IOException {
        long startTime0 = System.nanoTime();
        scanned_array = scanconversion.getDataFromInterpolation();
        long scanconversion_estimatedTime = System.nanoTime() - startTime0;
        //System.out.println("set scan conversion estimate " + scanconversion_estimatedTime);

        int[] colors = new int[scanned_array.length];
        int pixel;
        for(int i = 0; i < scanned_array.length; i++) {
            pixel = scanned_array[i];
            colors[i] = (pixel | (pixel << 8) | (pixel << 16)) | 0xFF000000;
        }

        final Bitmap bitmap = Bitmap.createBitmap(colors, Constants.PreProcParam.SCREEN_x, Constants.PreProcParam.SCREEN_z, Bitmap.Config.ARGB_8888);
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mainActionController.displayMainFrame(bitmap);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.echopen.asso.echopen.model.EchoImage.EchoIntImage;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.utils.Constants;

import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.IntBuffer;
import java.util.Arrays;

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

        //EchoIntImage echoImage = new EchoIntImage(scannedArray);
        //
        // final Bitmap bitmap = echoImage.createImage();
        int[] colors = new int[scannedArray.length];
        int pixel;
        for(int i = 0; i < scannedArray.length; i++) {
            pixel = scannedArray[i];
            colors[i] = (pixel | (pixel << 8) | (pixel << 16)) | 0xFF000000;
        }
        //Arrays.fill(colors, 0, 4*scannedArray.length, Color.WHITE);
        final Bitmap bitmap = Bitmap.createBitmap(colors, 512*2, 512/2, Bitmap.Config.ARGB_8888);

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

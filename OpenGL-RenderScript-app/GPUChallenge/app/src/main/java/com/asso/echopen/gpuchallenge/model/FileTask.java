package com.asso.echopen.gpuchallenge.model;

import android.app.Activity;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

import com.asso.echopen.gpuchallenge.preproc.ScanConversion;
import com.asso.echopen.gpuchallenge.ui.MainActionController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class refreshes the main UI to display data from a local file
 */
public class FileTask extends AbstractDataTask {

    private final Data data;

    public static int counter = 0;

    private RenderScript renderScript;

    public FileTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, InputStream inputStream) {
        super(activity, mainActionController, scanConversion);
        InputStreamReader isReader = new InputStreamReader(inputStream);
        data = new Data(isReader);
        scanconversion = new ScanConversion(data);
        ScanConversion.compute_tables();
        createScript();
    }

    private void createScript() {
        renderScript = RenderScript.create(activity);
        scanconversion.setRenderScript(renderScript);
}

    @Override
    protected Void doInBackground(Void... Voids) {
        while (true) {
            //For fun : scanconversion.randomize();
            long startTime0 = System.nanoTime();
            scanconversion.setData(data);
            long set_data_estimatedTime = System.nanoTime() - startTime0;
            System.out.println("set data time estimate " + set_data_estimatedTime);
            try {
                long startTime1 = System.nanoTime();
                refreshUI(scanconversion);
                long refresh_estimatedTime = System.nanoTime() - startTime1;
                System.out.println("set data refresh estimate " + refresh_estimatedTime);
                long estimatedTime = System.nanoTime() - startTime0;
                System.out.println("global time estimate " + estimatedTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//
//            }
        }
    }
}

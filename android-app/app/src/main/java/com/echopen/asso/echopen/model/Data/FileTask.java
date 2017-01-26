package com.echopen.asso.echopen.model.Data;

import android.app.Activity;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class refreshes the main UI to display data from a local file
 */
public class FileTask extends AbstractDataTask {

    private final Data data;

    public FileTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, InputStream inputStream) {
        super(activity, mainActionController, scanConversion);
        InputStreamReader isReader = new InputStreamReader(inputStream);
        data = new Data(isReader);
        scanconversion = new ScanConversion(data);
        ScanConversion.compute_tables();
    }

    @Override
    protected Void doInBackground(Void... Voids) {
        while (true) {
            //For fun : scanconversion.randomize();
            scanconversion.setData(data);
            refreshUI(scanconversion);
            //            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//
//            }
        }
    }
}

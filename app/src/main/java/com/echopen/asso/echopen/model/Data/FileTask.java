package com.echopen.asso.echopen.model.Data;

import android.app.Activity;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by loic on 19/12/15.
 */
public class FileTask extends AbstractDataTask {

    protected final Data data;

    public FileTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, InputStream inputStream) throws IOException {
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

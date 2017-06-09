package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.util.Log;

import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class refreshes the main UI to display data from a local file
 */
public class FileTask extends AbstractDataTask {

    private final String TAG = this.getClass().getSimpleName();
    private final Data data;

    public FileTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, InputStream inputStream, RenderingContextController iRenderingContextController) {
        super(activity, iRenderingContextController, null);
        InputStreamReader isReader = new InputStreamReader(inputStream);
        data = new Data(isReader);
    }

    @Override
    protected Void doInBackground(Void... Voids) {
        while (true) {
            RenderingContext lCurrentRenderingContext = mRenderingContextController.getCurrentRenderingContext();

            //refreshUI(lCurrentRenderingContext);
            //            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//
//            }
        }
    }
}

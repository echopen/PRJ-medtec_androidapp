package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.content.res.AssetManager;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mehdibenchoufi on 16/10/15.
 * This class connects with the UI of the MainActivity
 * handled by the MainActionController class. This is the parent of the classes that displays
 * image from data
 *
 * @todo implement a code pattern protocol-agnostic. Indeed, this should store the generic variables and methods
 * that will be inherited by classes that will be protocol specific. See UDPToBitmapDisplayer class for an example
 * of a class that  displays image from UDP data
 */
public class Displayer {

    protected Activity activity;

    protected MainActionController mainActionController;

    protected InputStreamReader inputStreamReader;

    protected ScanConversion scanConversion;

    public Displayer(Activity activity, MainActionController mainActionController) {
        this.activity = activity;
        this.mainActionController =  mainActionController;
        setAssetManager(activity);
    }

    private void setAssetManager(Activity activity) {
        AssetManager assetManager = activity.getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("data/raw_data/r_sigmoy.csv");
            inputStreamReader = new InputStreamReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

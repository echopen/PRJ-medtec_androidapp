package com.asso.echopen.gpuchallenge.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.asso.echopen.gpuchallenge.R;
import com.asso.echopen.gpuchallenge.utils.Config;

public class MainActionController {

    private Activity activity;

    public MainActionController() {
        displayAction();
    }

    public MainActionController(Activity activity) {
        this.activity = activity;
        displayAction();
    }

    private void displayAction(){}

    private View findViewById(int id){
        return this.activity.findViewById(id);
    }

    public void displayMainFrame(Bitmap bitmap){
        //ImageView echoImage = (ImageView) findViewById(R.id.echo);
        //echoImage.setImageBitmap(bitmap);
        //echoImage.setColorFilter(Config.colorMatrixColorFilter);
    }
}
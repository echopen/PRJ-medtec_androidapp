package com.echopen.asso.echopen.model;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mehdibenchoufi on 30/06/16.
 */
public class Synchronizer {

    public static Synchronizer singletonSynchronizer = null;

    private static Activity activity;

    private Synchronizer(Activity activity){
        this.activity = activity;
    }

    private View findViewById(int id){
        return this.activity.findViewById(id);
    }

    public static Synchronizer getInstance(Activity activity) {
        if (singletonSynchronizer == null) {
            singletonSynchronizer = new Synchronizer(activity);
        }
        return singletonSynchronizer;
    }

    public void synchronizeVisibility(int textId){
        TextView textView = (TextView) findViewById(textId);
        textView.setVisibility(View.VISIBLE);
    }

    public void synchronizeTextAndImage(int textId, String measure){
        TextView textView = (TextView) findViewById(textId);
        textView.setTextColor(Color.WHITE);
        textView.setText(measure + " cm");
    }
}

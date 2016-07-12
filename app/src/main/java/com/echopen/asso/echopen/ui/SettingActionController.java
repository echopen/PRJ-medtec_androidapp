package com.echopen.asso.echopen.ui;

import android.app.Activity;
import android.view.View;

import com.echopen.asso.echopen.utils.TouchEffect;


public class SettingActionController {

    private Activity activity;

    public static final TouchEffect TOUCH = new TouchEffect();


    public SettingActionController() {
        displayAction();
    }

    public SettingActionController(Activity activity) {
        this.activity = activity;
        displayAction();
    }

    private void displayAction(){}

    private View findViewById(int id){
        return this.activity.findViewById(id);
    }

    public void displayHeader(){}

    public void displayHeader(View view, Activity activity){
    }

}

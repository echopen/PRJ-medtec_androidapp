package com.asso.echopen.gpuchallenge.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

/**
 * Created by mehdibenchoufi on 26/01/17.
 */

public class OpenGLCheck {
    public boolean checkHasGLES20(Context context){
        return hasGLES20(context);
    }

    private boolean hasGLES20(Context context) {
        ActivityManager am = (ActivityManager) context.
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }
}

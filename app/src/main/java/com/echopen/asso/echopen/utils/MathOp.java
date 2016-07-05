package com.echopen.asso.echopen.utils;

import android.util.DisplayMetrics;

/**
 * Created by mehdibenchoufi on 01/07/16.
 */
public class MathOp {

    public MathOp() {
    }

    public static float getRealMeasure(float pixels, DisplayMetrics displayMetrics){
        float densityDpi = (float) displayMetrics.densityDpi;
        float inches = pixels / densityDpi;
        return inches * 2.54f;
    }
}

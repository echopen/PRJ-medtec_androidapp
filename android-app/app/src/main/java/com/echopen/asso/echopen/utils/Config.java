package com.echopen.asso.echopen.utils;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Singleton Method for Configuration
 */
public class Config {

    public static Config singletonConfig = null;

    private static Context context;

    private int height;

    private int width;

    public static ColorMatrixColorFilter colorMatrixColorFilter;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private static DisplayMetrics metrics;

    private Config(Context context){
        this.context = context;
    }

    public static Config getInstance(Context context) {
        if (singletonConfig == null) {
            singletonConfig = new Config(context);
            initDisplayInfo(context);
            singletonConfig.setHeightAndWidth();
            singletonConfig.setColorFilter();
        }
        return singletonConfig;
    }

    /* todo : we need to check if this supports any version
     * perhaps we need to check if : Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2
     */
    private static void initDisplayInfo(Context context){
        metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
    }

    private void setHeightAndWidth(){
        this.height = metrics.heightPixels;
        this.width = metrics.widthPixels;
    }

    /* Set grayscale color filter. This is used to filter the Bitmap echo Image
     * in grayscale mode. See `MainActionController`for usage
     */
    private void setColorFilter(){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        colorMatrixColorFilter = new ColorMatrixColorFilter(matrix);
    }
}

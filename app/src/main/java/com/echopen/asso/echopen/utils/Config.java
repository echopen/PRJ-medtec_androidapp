package com.echopen.asso.echopen.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Singleton Method for Configuration
 */
public class Config {

    private static Config config = null;

    private static Context context;

    private int height;

    private int width;

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
        if (config == null) {
            config = new Config(context);
            initDisplayInfo(context);
            config.setHeightAndWidth();
        }
        return config;
    }

    /* todo : we need to check if this supports any version
     * perhaps we need to check if : Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2
     * */
    private static void initDisplayInfo(Context context){
        metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
    }

    private void setHeightAndWidth(){
        this.height = metrics.heightPixels;
        this.width = metrics.widthPixels;
    }
}

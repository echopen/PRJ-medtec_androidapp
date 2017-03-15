package com.echopen.asso.echopen.utils;

import android.util.Log;

/**
 * Created by mehdibenchoufi on 04/04/16.
 *
 * This is logging utility, in order to monitor app performance
 */
public class Timer {

    public static boolean timer_status = false;
    private static long time;
    private static String mark;

    public Timer() {
    }

    public static void init(String label){
        if(timer_status) {
            time = System.nanoTime();
            mark = label;
        }
    }

    public static void logResult(){
        if(timer_status) {
            long completedIn = System.nanoTime() - time;
            Log.d("time of computation for " + mark, String.valueOf(completedIn));
            time = System.nanoTime() - time;
        }
    }
}
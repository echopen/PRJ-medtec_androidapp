package com.echopen.asso.echopen.utils;

import android.util.Log;

/**
 * Created by mehdibenchoufi on 04/04/16.
 *
 * This is logging utility, in order to monitor app performance
 */
public class Timer {

    public static boolean timer_status = true;
    private static long time;
    private static String mTimerLabel;

    public Timer() {
    }

    public static void init(String iTimerLabel){
        if(timer_status) {
            time = System.nanoTime();
            mTimerLabel = iTimerLabel;
        }
    }

    public static void logResult(String iTimerTag){
        if(timer_status) {
            long completedIn = System.nanoTime() - time;
            Log.d("Timer: "+mTimerLabel, "time of computation for -" + iTimerTag +": " + String.valueOf(completedIn/1000000) + "ms");
            //time = System.nanoTime() - time;
        }
    }
}
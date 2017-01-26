package com.asso.echopen.gpuchallenge.utils;

import android.util.Log;

/**
 * Created by mehdibenchoufi on 04/04/16.
 *
 * This is logging utility, in order to monitor app performance
 */
public class Timer {

    private static long time;
    private static String mark;

    public Timer() {
    }

    public void init(String mark){
        this.time = System.nanoTime();
        this.mark = mark;
    }

    public void logResult(){
        long completedIn = System.nanoTime() - time;
        Log.d("this is time of computation for " + mark, String.valueOf(completedIn));
    }
}
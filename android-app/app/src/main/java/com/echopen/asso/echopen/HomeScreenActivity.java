package com.echopen.asso.echopen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

// Probe imports

/**
 * Created by yanis on 04/07/2017.
 */

public class HomeScreenActivity extends Activity{

    private float x1;
    private float x2;

    private float y1;
    private float y2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
    }

    /**
     * Start home features
     */
    private void startHome()
    {

    }

    public boolean onTouchEvent(MotionEvent touchevent) {

        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if ((x1 < x2) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "RIGHT");
                }
                if ((x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                }
                if ((y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                }
                if ((y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                }
            }
        }
        return false;
    }
}

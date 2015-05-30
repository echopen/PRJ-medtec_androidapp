package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;


public class SplashScreen extends Activity
{

    private boolean isRunning;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        isRunning = true;

        startSplash();
    }

    private void startSplash()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(3000);
                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            doFinish();
                        }
                    });
                }
            }
        }).start();
    }

    private synchronized void doFinish()
    {
        if (isRunning)
        {
            isRunning = false;
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            isRunning = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
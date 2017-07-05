package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;

/**
 * This class holds for a custom SplashScreen
 */
public class chooseProfile extends Activity
{
    private CheckBox checkBoxHomme;
    private CheckBox checkBoxFemme;

    /* Reflects the state of the activity, when set to false, the activity will finish soon */
    private boolean isRunning;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_profile);

        checkBoxHomme = (CheckBox) findViewById(R.id.checkbox_homme);
        checkBoxFemme = (CheckBox) findViewById(R.id.checkbox_femme);


    }

    /**
     * A thread calls doFinish() method after 3000s spleeping
     * Splash picture will be displayed inbetween
     * Then the Mainactivity is launched
     */
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

    /**
     * kicks the Mainactivity intent
     */
    private synchronized void doFinish()
    {
        if (isRunning)
        {
            isRunning = false;
            Intent i = new Intent(chooseProfile.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    /*
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

    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.checkbox_femme:
                if (checked) {
                    Log.d("cool", "1");
                    checkBoxHomme.setChecked(false);
                }
                else {
                    Log.d("cool", "2");
                }
                break;
            case R.id.checkbox_homme:
                if (checked) {
                    Log.d("cool", "3");
                    checkBoxFemme.setChecked(false);
                }
                else {
                    Log.d("cool", "4");
                }
                break;
        }
    }

}
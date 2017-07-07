package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * This class holds for a custom SplashScreen
 */
public class connectDevice extends Activity
{


    /* Reflects the state of the activity, when set to false, the activity will finish soon */
    private boolean isRunning;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_connection);

        Button closeConnectDevice = (Button) findViewById(R.id.closeConnectDeviceBt);
        Button profileActivity = (Button) findViewById(R.id.profileBt);

        TextView textViewHome = (TextView) findViewById(R.id.rich_text);
        closeConnectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Intent Login = new Intent(connectDevice.this, Login.class);
                //startActivity(Login);
            }
        });

        profileActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent chooseProfile = new Intent(connectDevice.this, chooseProfile.class);
                startActivity(chooseProfile);
            }
        });



        //textViewHome span color
        final SpannableStringBuilder sb = new SpannableStringBuilder("Connect your echo-stethoscope to start an exam.");
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(46,106,255));
        sb.setSpan(fcs, 13, 29, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textViewHome.setText(sb);
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

    }
}
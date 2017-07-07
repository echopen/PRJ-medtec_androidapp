package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.echopen.asso.echopen.SettingsActivity;
/**
 * MainActivity class handles the main screen of the app.
 * Tools are called in the following order :
 * - initSwipeViews() handles the gesture tricks via GestureDetector class
 * - initViewComponents() mainly sets the clickable elements
 * - initActionController() and setupContainer() : in order to separate concerns, View parts are handled by the initActionController()
 * method which calls the MainActionController class that deals with MainActivity Views,
 * especially handles the display of the main screen picture
 * These two methods should be refactored into one
 */

public class MainActivity extends Activity implements View.OnClickListener{
    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton start_button = (ImageButton) findViewById(R.id.button_start);
        start_button.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.title);
        TextView welcome_text = (TextView) findViewById(R.id.welcome_text);
        TextView input_text_1 = (TextView) findViewById(R.id.input_text_1);
        TextView input_text_2 = (TextView) findViewById(R.id.input_text_2);
        TextView input_1 = (TextView) findViewById(R.id.input_1);
        TextView input_2 = (TextView) findViewById(R.id.input_2);
        TextView link_1 = (TextView) findViewById(R.id.link_1);
        TextView link_2 = (TextView) findViewById(R.id.link_2);

        setFont(title,"Moderat-Bold.ttf");
        setFont(welcome_text,"Avenir-Book.ttf");
        setFont(input_text_1,"Avenir-Medium.ttf");
        setFont(input_text_2,"Avenir-Medium.ttf");
        setFont(input_1,"Avenir-Medium.ttf");
        setFont(input_2,"Avenir-Medium.ttf");
        setFont(link_1,"Avenir-Medium.ttf");
        setFont(link_2,"Avenir-Medium.ttf");
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    /**
     * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
     * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.”,
     * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     *
     * @param requestCode, integer argument that identifies your request
     * @param resultCode, to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
     * @param data,       Intent instance
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.button_start:
                startActivity(new Intent( this, DashboardActivity.class));
                break;
        }
    }

    public void setFont(TextView textView, String fontName) {
        if(fontName != null){
            try {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + fontName);
                textView.setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }
}
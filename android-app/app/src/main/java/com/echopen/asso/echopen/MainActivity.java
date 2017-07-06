package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FragmentManager fragmentmanager;

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

        fragmentmanager = getSupportFragmentManager();

        Button enterBt = (Button) findViewById(R.id.enter_bt);
        enterBt.setOnClickListener(this);

        Button signupBt = (Button) findViewById(R.id.signup_bt);
        signupBt.setOnClickListener(this);

        Button signintBt = (Button) findViewById(R.id.signin_bt);
        signintBt.setOnClickListener(this);

        Button emergencyBt = (Button) findViewById(R.id.emergency_bt);
        emergencyBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.signup_bt :
                btSignUpClicked();
                break;
            case R.id.signin_bt :
                btSignInClicked();
                break;
            case R.id.emergency_bt :
                btEmergencyClicked();
                break;
            case R.id.enter_bt :
                btEnterClicked();
                break;
        }
    }

    private void btSignUpClicked(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

        //SignUpFragment signUpFragment = new SignUpFragment();
        //fragmentmanager.beginTransaction().replace(R.id.fragmentlayout, signUpFragment).commit();
    }

    private void btSignInClicked(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //SignUpFragment signUpFragment = new SignInFragment();
        //fragmentmanager.beginTransaction().replace(R.id.fragmentlayout, signInFragment).commit();
    }

    private void btEmergencyClicked(){
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }

    private void btEnterClicked(){
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
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
}
package com.echopen.asso.echopen;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.signpost.http.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class holds for a custom SplashScreen
 */
public class chooseProfile extends Activity
{
    //gender
    private CheckBox checkBoxMal;
    private CheckBox checkBoxFemale;

    //age
    private CheckBox checkBoxBaby;
    private CheckBox checkBoxKid;
    private CheckBox checkBoxAdult;

    //fatness
    private CheckBox checkBoxSmall;
    private CheckBox checkBoxMedium;
    private CheckBox checkBoxLarge;

    //organ
    private CheckBox checkBoxUroDigestive;
    private CheckBox checkBoxCardiac;
    private CheckBox checkBoxThoracic;
    private CheckBox checkBoxObstetrics;
    private CheckBox checkBoxOsteoarthritis;

    //choices
    private String choiceGender = "";
    private String choiceAge = "";
    private String choiceFatness = "";
    private String choiceOrgan = "";

    //scroll Y
    private Integer genderY = 170;
    private Integer ageY = 570;

    private ScrollView scrollView;

    /* Reflects the state of the activity, when set to false, the activity will finish soon */
    private boolean isRunning;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_profile);

        //gender
        checkBoxMal = (CheckBox) findViewById(R.id.checkbox_mal);
        checkBoxFemale = (CheckBox) findViewById(R.id.checkbox_female);

        //age
        checkBoxBaby = (CheckBox) findViewById(R.id.checkbox_baby);
        checkBoxKid = (CheckBox) findViewById(R.id.checkbox_kid);
        checkBoxAdult = (CheckBox) findViewById(R.id.checkbox_adult);

        //fatness
        checkBoxSmall = (CheckBox) findViewById(R.id.checkbox_small);
        checkBoxMedium = (CheckBox) findViewById(R.id.checkbox_medium);
        checkBoxLarge = (CheckBox) findViewById(R.id.checkbox_large);

        //organ
        checkBoxUroDigestive = (CheckBox) findViewById(R.id.checkbox_uro_digestive);
        checkBoxCardiac = (CheckBox) findViewById(R.id.checkbox_cardiac);
        checkBoxThoracic = (CheckBox) findViewById(R.id.checkbox_thoracic);
        checkBoxObstetrics = (CheckBox) findViewById(R.id.checkbox_obstetrics);
        checkBoxOsteoarthritis = (CheckBox) findViewById(R.id.checkbox_osteoarthritis);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        //textViewHome span color
        TextView textViewHome = (TextView) findViewById(R.id.rich_text);
        final SpannableStringBuilder sb = new SpannableStringBuilder("Select your patient informations to pre-set your settings");
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(46,106,255));
        sb.setSpan(fcs, 12, 32, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textViewHome.setText(sb);

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

            //gender
            case R.id.checkbox_female:
                if (checked) {
                    Log.d("checkbox", "female check");
                    checkBoxMal.setChecked(false);
                    choiceGender = "female";
                    ObjectAnimator.ofInt(scrollView, "scrollY",  genderY).setDuration(300).start();
                }
                else {
                    Log.d("checkbox", "female unchecked");
                    choiceGender = "";
                }
                break;
            case R.id.checkbox_mal:
                if (checked) {
                    Log.d("checkbox", "mal checked");
                    checkBoxFemale.setChecked(false);
                    choiceGender = "mal";
                    ObjectAnimator.ofInt(scrollView, "scrollY",  genderY).setDuration(300).start();
                }
                else {
                    Log.d("checkbox", "mal unchecked");
                    choiceGender = "";
                }
                break;

            //age
            case R.id.checkbox_baby:
                if (checked) {
                    Log.d("checkbox", "baby checked");
                    checkBoxKid.setChecked(false);
                    checkBoxAdult.setChecked(false);
                    choiceAge = "baby";
                    ObjectAnimator.ofInt(scrollView, "scrollY",  ageY).setDuration(300).start();
                }
                else {
                    Log.d("checkbox", "baby unchecked");
                    choiceAge = "";
                }
                break;
            case R.id.checkbox_kid:
                if (checked) {
                    Log.d("checkbox", "kid checked");
                    checkBoxBaby.setChecked(false);
                    checkBoxAdult.setChecked(false);
                    choiceAge = "kid";
                    ObjectAnimator.ofInt(scrollView, "scrollY",  ageY).setDuration(300).start();
                }
                else {
                    Log.d("checkbox", "kid unchecked");
                    choiceAge = "";
                }
                break;
            case R.id.checkbox_adult:
                if (checked) {
                    Log.d("checkbox", "adult checked");
                    checkBoxBaby.setChecked(false);
                    checkBoxKid.setChecked(false);
                    choiceAge = "adult";
                    ObjectAnimator.ofInt(scrollView, "scrollY",  ageY).setDuration(300).start();
                }
                else {
                    Log.d("checkbox", "adult unchecked");
                    choiceAge = "";
                }
                break;


            //fatness
            case R.id.checkbox_small:
                if (checked) {
                    Log.d("checkbox", "small checked");
                    checkBoxMedium.setChecked(false);
                    checkBoxLarge.setChecked(false);
                    choiceFatness = "small";
                }
                else {
                    Log.d("checkbox", "small unchecked");
                    choiceFatness = "";
                }
                break;
            case R.id.checkbox_medium:
                if (checked) {
                    Log.d("checkbox", "medium checked");
                    checkBoxSmall.setChecked(false);
                    checkBoxLarge.setChecked(false);
                    choiceFatness = "medium";
                }
                else {
                    Log.d("checkbox", "medium unchecked");
                    choiceFatness = "";
                }
                break;
            case R.id.checkbox_large:
                if (checked) {
                    Log.d("checkbox", "large checked");
                    checkBoxSmall.setChecked(false);
                    checkBoxMedium.setChecked(false);
                    choiceFatness = "large";
                }
                else {
                    Log.d("checkbox", "large unchecked");
                    choiceFatness = "";
                }
                break;


            //organ
            case R.id.checkbox_uro_digestive:
                if (checked) {
                    Log.d("checkbox", "uro_digestive checked");
                    checkBoxCardiac.setChecked(false);
                    checkBoxThoracic.setChecked(false);
                    checkBoxObstetrics.setChecked(false);
                    checkBoxOsteoarthritis.setChecked(false);
                    choiceOrgan = "uro_digestive";
                }
                else {
                    Log.d("checkbox", "uro_digestive unchecked");
                    choiceOrgan = "";
                }
                break;
            case R.id.checkbox_cardiac:
                if (checked) {
                    Log.d("checkbox", "cardiac checked");
                    checkBoxUroDigestive.setChecked(false);
                    checkBoxThoracic.setChecked(false);
                    checkBoxObstetrics.setChecked(false);
                    checkBoxOsteoarthritis.setChecked(false);
                    choiceOrgan = "cardiac";
                }
                else {
                    Log.d("checkbox", "cardiac unchecked");
                    choiceOrgan = "";
                }
                break;
            case R.id.checkbox_thoracic:
                if (checked) {
                    Log.d("checkbox", "thoracic checked");
                    checkBoxUroDigestive.setChecked(false);
                    checkBoxCardiac.setChecked(false);
                    checkBoxObstetrics.setChecked(false);
                    checkBoxOsteoarthritis.setChecked(false);
                    choiceOrgan = "thoracic";
                }
                else {
                    Log.d("checkbox", "thoracic unchecked");
                    choiceOrgan = "";
                }
                break;
            case R.id.checkbox_obstetrics:
                if (checked) {
                    Log.d("checkbox", "large checked");
                    checkBoxUroDigestive.setChecked(false);
                    checkBoxCardiac.setChecked(false);
                    checkBoxThoracic.setChecked(false);
                    checkBoxOsteoarthritis.setChecked(false);
                    choiceOrgan = "large";
                }
                else {
                    Log.d("checkbox", "large unchecked");
                    choiceOrgan = "";
                }
                break;
            case R.id.checkbox_osteoarthritis:
                if (checked) {
                    Log.d("checkbox", "osteoarthritis checked");
                    checkBoxUroDigestive.setChecked(false);
                    checkBoxCardiac.setChecked(false);
                    checkBoxThoracic.setChecked(false);
                    checkBoxObstetrics.setChecked(false);
                    choiceOrgan = "osteoarthritis";
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
                else {
                    Log.d("checkbox", "osteoarthritis unchecked");
                    choiceOrgan = "";
                }
                break;
        }
    }

    public void validChoice(View view) {
        String[] choices = {choiceGender, choiceAge, choiceFatness, choiceOrgan};
        System.out.println(Arrays.toString(choices));
        if (!choiceGender.equals("") && !choiceAge.equals("") && !choiceFatness.equals("") && !choiceOrgan.equals("")){
            Log.d("test", "redirect to device camera");
        } else {
            Log.d("test", "profile not complete");
        }
    }
}
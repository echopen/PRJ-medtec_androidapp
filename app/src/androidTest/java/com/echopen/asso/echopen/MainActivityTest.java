package com.echopen.asso.echopen;

import android.app.Application;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends android.support.test.runner.AndroidJUnitRunner {
    
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void checkIfMainViewsExists() throws NoMatchingViewException {
        onView(withId(R.id.btnCapture)).check(doesNotExist());
        onView(withId(R.id.btnEffect)).check(doesNotExist());
        onView(withId(R.id.btnPic)).check(doesNotExist());
        onView(withId(R.id.tabBrightness)).check(doesNotExist());
        onView(withId(R.id.tabGrid)).check(doesNotExist());
        onView(withId(R.id.tabSetting)).check(doesNotExist());
        onView(withId(R.id.tabSuffle)).check(doesNotExist());
        onView(withId(R.id.tabTime)).check(doesNotExist());
        onView(withId(R.id.btn1)).check(doesNotExist());
        onView(withId(R.id.btn2)).check(doesNotExist());
        onView(withId(R.id.btn3)).check(doesNotExist());
        onView(withId(R.id.btn4)).check(doesNotExist());
        onView(withId(R.id.btn5)).check(doesNotExist());
    }
}

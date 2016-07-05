package com.echopen.asso.echopen;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.PowerManager;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.echopen.asso.echopen.ui.ConstantDialogFragment;
import com.echopen.asso.echopen.utils.Config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private ConstantDialogFragment constantDialogFragment;

    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
    }

    /**
     * Check if the singleton class Config is loaded when the activity starts
     */
    @Test
    public void checkIfConfigIsLoaded() {
        assertNotNull(Config.singletonConfig);
    }

    /**
     * Check height and width types served by the singleton class Config
     */
    @Test
    public void checkConfigParams() {
        int height = Config.singletonConfig.getHeight();
        int width = Config.singletonConfig.getWidth();
        assertEquals((height > 0) & (width > 0), true);
    }

    /**
     * Checking main UI buttons
     *
     * @throws NoMatchingViewException
     */
    @Test
    public void checkIfMainViewsExists() throws NoMatchingViewException {
        onView(withId(R.id.btnCapture)).check(doesNotExist());
        onView(withId(R.id.btnEffect)).check(doesNotExist());
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

        onView(withId(R.id.seekBar)).check(doesNotExist());
        onView(withId(R.id.seekBar2)).check(doesNotExist());
        onView(withId(R.id.seekBar3)).check(doesNotExist());
    }
}

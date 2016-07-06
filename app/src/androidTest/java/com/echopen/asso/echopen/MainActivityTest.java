package com.echopen.asso.echopen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.echopen.asso.echopen.custom.utils.TestHelper;
import com.echopen.asso.echopen.ui.ConstantDialogFragment;
import com.echopen.asso.echopen.utils.Config;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.echopen.asso.echopen.custom.utils.TestHelper.checkUiDoNotExist;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */

@SmallTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private ConstantDialogFragment constantDialogFragment;

    private MainActivity mainActivity;

    private List<Integer> listUi;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        listUi = new ArrayList<>();
    }

    /**
     * Check if the singleton class Config is loaded when the activity starts
     */
    public void testIfConfigIsLoadedTest() {
        assertNotNull(Config.singletonConfig);
    }

    /**
     * Check height and width types served by the singleton class Config
     */
    public void testConfigParams() {
        int height = Config.singletonConfig.getHeight();
        int width = Config.singletonConfig.getWidth();
        assertEquals((height > 0) & (width > 0), true);
    }

    /**
     * Check if the background color is indeed transparent
     */
    public void testLayoutBackgroundColor() throws InterruptedException {
        dismissTheAlertDialogBox();
        onView(withId(R.id.vMiddle)).check(matches(withLayoutBackgroundColor(Color.TRANSPARENT)));
    }

    private void dismissTheAlertDialogBox() {
        onView(withText("Cancel")).
                perform(click());
    }

    /**
     * Checking main UI buttons
     *
     * @throws NoMatchingViewException
     */
    public void testBareMainViewsExists() throws NoMatchingViewException {
        dumpUi(R.id.btnEffect, R.id.tabBrightness, R.id.tabGrid, R.id.tabSetting,
                R.id.tabSuffle, R.id.tabTime, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.seekBar, R.id.seekBar2, R.id.seekBar3);

        TestHelper.checkUiDoNotExist(listUi);
    }

    public void testMainViewsExists() throws NoMatchingViewException {
        dismissTheAlertDialogBox();
        dumpUi(/*R.id.btnEffect,*/ R.id.tabBrightness, /*R.id.tabGrid,*/ R.id.tabSetting,
                R.id.tabSuffle, R.id.tabTime, R.id.btn1, R.id.btn2, R.id.btn3/*, R.id.btn4, R.id.btn5*/);

        TestHelper.checkUiIsDisplayed(listUi);
    }

    public static Matcher<View> withLayoutBackgroundColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, LinearLayout>(LinearLayout.class) {
            @Override
            protected boolean matchesSafely(LinearLayout linearLayout) {
                Drawable background = linearLayout.getBackground();
                int layout_color = ((ColorDrawable) background).getColor();
                return  color == layout_color;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with this color: ");
            }
        };
    }

    private void dumpUi(int... args) {
        for(int elem : args){
            listUi.add(elem);
        }
    }
}

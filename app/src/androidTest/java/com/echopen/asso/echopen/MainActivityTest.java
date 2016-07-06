package com.echopen.asso.echopen;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
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
    @Test
    public void testIfConfigIsLoadedTest() {
        assertNotNull(Config.singletonConfig);
    }

    /**
     * Check height and width types served by the singleton class Config
     */
    @Test
    public void testConfigParams() {
        int height = Config.singletonConfig.getHeight();
        int width = Config.singletonConfig.getWidth();
        assertEquals((height > 0) & (width > 0), true);
    }

    /**
     * Check if the background color is indeed transparent
     */
    @Test
    public void testLayoutBackgroundColor() throws InterruptedException {
        //onView(withId(R.id.vMiddle)).check(matches(withLayoutBackgroundColor(Color.RED)));
        //onView(withId(R.id.vMiddle)).check(matches(isDisplayed()));
    }
    /**
     * Checking main UI buttons
     *
     * @throws NoMatchingViewException
     */
    @Test
    public void testMainViewsExists() throws NoMatchingViewException {
        dumpUi(R.id.btnEffect, R.id.tabBrightness, R.id.tabGrid, R.id.tabSetting,
                R.id.tabSuffle, R.id.tabTime, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.seekBar, R.id.seekBar2, R.id.seekBar3);

        TestHelper.checkUiDoNotExist(listUi);
        onView(withId(R.id.btnCapture)).check(matches(isDisplayed()));
    }

    public static Matcher<View> withLayoutBackgroundColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, LinearLayout>(LinearLayout.class) {
            @Override
            protected boolean matchesSafely(LinearLayout linearLayout) {
                return false;
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

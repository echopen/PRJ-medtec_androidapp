package com.echopen.asso.echopen;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.echopen.asso.echopen.model.Painter.SelfPaint;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.TestHelper;
import com.robotium.solo.Solo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * main MainActivity Tests : test the UI and the ScanConversion algorithm, depending on the
 * chosen protocol processing from Local data file or through UDP, TCP protocol.
 */

@SmallTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

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
        /* When emulator screen is not un locked, tests won't pass throwing
        a message "RuntimeException: Waited for the root of the view hierarchy to have window focus
        Had to install and use Robotium*/
        Solo solo = new Solo(getInstrumentation(), getActivity());
        solo.unlockScreen();
    }

    /**
     * Check whether the boolean related protocol value are correctly set, namely
     * LOCAL_ACQUISITION = true, TCP_ACQUISITION = false, UDP_ACQUISITION = false;
     */
    public void testBooleanProtocolValues(){
        assertEquals((MainActivity.LOCAL_ACQUISITION) & (!MainActivity.TCP_ACQUISITION) & (!MainActivity.UDP_ACQUISITION), true);

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
     * Check whether mainActionController is instantiated
     */
    public void testMainActionController() throws NoSuchFieldException, IllegalAccessException {
        Field field = mainActivity.getClass().getDeclaredField("mainActionController");
        field.setAccessible(true);
        Object value = field.get(mainActivity);
        assertNotNull(value);
    }


    /**
     * Check if the background color is indeed transparent
     */
    public void LayoutBackgroundColor() throws InterruptedException {
        TestHelper.dismissTheAlertDialogBox();
        onView(withId(R.id.vMiddle)).check(matches(withLayoutBackgroundColor()));
    }

    /**
     * Checking main UI buttons without dismissing alert dialog box
     * @throws NoMatchingViewException
     */
    public void BareMainViewsExists() throws NoMatchingViewException {
        dumpUi(R.id.btnEffect, R.id.tabBrightness, R.id.tabGrid, R.id.tabSetting,
                R.id.tabSuffle, R.id.tabTime, R.id.btn1);

        TestHelper.checkUiDoNotExist(listUi);
    }

    /**
     * Checking main UI buttons after dismissing alert dialog box
     * @throws NoMatchingViewException
     */
    public void MainViewsExists() throws NoMatchingViewException {
        TestHelper.dismissTheAlertDialogBox();
        dumpUi(/*R.id.btnEffect,*/ R.id.tabBrightness, /*R.id.tabGrid,*/ R.id.tabSetting,
                R.id.tabSuffle, R.id.tabTime, R.id.btn1);

        TestHelper.checkUiIsDisplayed(listUi);
    }

    /**
     * This UI test is separated from others since tits visibility is controlled
     * by mainActionController instance
     */
    public void MeasureTextViewIsVisible(){
        TestHelper.dismissTheAlertDialogBox();
        dumpUi(R.id.measure);
        TestHelper.checkUiIsVisible(listUi);
    }

    /**
     * Matcher to test the Background color
     * @return
     */
    private static Matcher<View> withLayoutBackgroundColor() {
        return new BoundedMatcher<View, LinearLayout>(LinearLayout.class) {
            @Override
            protected boolean matchesSafely(LinearLayout linearLayout) {
                Drawable background = linearLayout.getBackground();
                int layout_color = ((ColorDrawable) background).getColor();
                return  Color.TRANSPARENT == layout_color;
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

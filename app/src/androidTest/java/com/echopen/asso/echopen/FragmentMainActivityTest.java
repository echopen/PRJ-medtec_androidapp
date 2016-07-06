package com.echopen.asso.echopen;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.Button;

import com.echopen.asso.echopen.ui.ConstantDialogFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by mehdibenchoufi on 27/07/15.
 *
 * Dedicated to test AlertDialog Fragment logic
 */

@SmallTest
public class FragmentMainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private ConstantDialogFragment constantDialogFragment;

    private MainActivity mainActivity;

    /*@Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);*/

    public FragmentMainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        constantDialogFragment = (ConstantDialogFragment) getActivity().getFragmentManager().findFragmentByTag("fragment_edit_name");
    }

    public void testConstantDialogFragment() {
        assertTrue(constantDialogFragment instanceof ConstantDialogFragment);
        assertTrue(constantDialogFragment.getShowsDialog());
    }

    public void testDataIsFetchedFromDialogAlertBox() throws Throwable {
        AlertDialog alertDialog = constantDialogFragment.getAlertDialog();
        doClick(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE));

        if (alertDialog.isShowing()) {
            try {
                onView(withId(R.id.btnCapture)).check(doesNotExist());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @UiThreadTest
    private void doClick(final Button button) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    button.performClick();
                    onView(withId(R.id.btnCapture)).check(doesNotExist());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //onView(withId(R.id.btnCapture)).check(doesNotExist());
        //getInstrumentation().waitForIdleSync();
    }
}


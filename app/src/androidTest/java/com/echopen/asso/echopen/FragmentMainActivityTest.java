package com.echopen.asso.echopen;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.echopen.asso.echopen.ui.ConstantDialogFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by mehdibenchoufi on 27/07/15.
 *
 * Dedicated to test AlertDialog Fragment logic
 */

@SmallTest
public class FragmentMainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private ConstantDialogFragment constantDialogFragment;

    private MainActivity mainActivity;

    private AlertDialog alertDialog;

    public FragmentMainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        constantDialogFragment = (ConstantDialogFragment) getActivity().getFragmentManager().findFragmentByTag("fragment_edit_name");
        alertDialog = constantDialogFragment.getAlertDialog();
    }

    public void testConstantDialogFragment() {
        assertTrue(constantDialogFragment instanceof ConstantDialogFragment);
        assertTrue(constantDialogFragment.getShowsDialog());
    }

    /**
     * Clicking on local checkbox triggers ScanConversion on the data stored in a local CSV file
     */
    public void testDataIsFetchedFromLocal() throws Throwable {
        ListView listView = alertDialog.getListView();
        View child = listView.getChildAt(0);
        clickAndValidate(child);
    }

    /**
     * Clicking on TCP checkbox triggers ScanConversion on the data stored sent
     * from the hardware through TCP protocol
     */
    public void testDataIsFetchedFromDialogTCP() throws Throwable {
        ListView listView = alertDialog.getListView();
        View child = listView.getChildAt(1);
        clickAndValidate(child);
    }

    /**
     * Clicking on UDP checkbox triggers ScanConversion on the data stored sent
     * from the hardware through UDP protocol
     */
    public void testDatalIsFetchedFromUDP() throws Throwable {
        ListView listView = alertDialog.getListView();
        View child = listView.getChildAt(2);
        clickAndValidate(child);
    }

    /**
     * The UI is intended to be more extensively explored when
     * the alert dialog is dismissed. The following tests are here just as examples
     */
    public void testCancelClick() {
        onView(withText("Cancel")).
                perform(click());
        onView(withId(R.id.btnCapture)).check(matches(isDisplayed()));
    }

    private void clickAndValidate(View child) {
        boolean thrown = false;
        child.performClick();
        try {
            onView(withText("Ok")).
                    perform(click());
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}


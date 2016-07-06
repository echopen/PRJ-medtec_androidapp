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
import android.widget.Button;

import com.echopen.asso.echopen.ui.ConstantDialogFragment;

import org.junit.Rule;
import org.junit.Test;
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
        doClick();

        if (alertDialog.isShowing()) {
            try {
                onView(withId(R.id.btnCapture)).check(doesNotExist());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The UI is intended to be more extensively explored when
     * the alert dialog is dismissed. The following tests are here just as examples
     */
    private void doClick() {
        onView(withText("Cancel")).
                perform(click());
        onView(withId(R.id.btnCapture)).check(matches(isDisplayed()));
    }
}


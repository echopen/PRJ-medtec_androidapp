package com.echopen.asso.echopen;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.echopen.asso.echopen.ui.ConstantDialogFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mehdibenchoufi on 27/07/15.
 *
 * Dedicated to test AlertDialog Fragment logic
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FragmentMainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private ConstantDialogFragment constantDialogFragment;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    public FragmentMainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        constantDialogFragment = (ConstantDialogFragment) mActivityRule.getActivity().getFragmentManager().findFragmentByTag("fragment_edit_name");
    }

    @Test
    public void checkConstantDialogFragment() {
        assertTrue(constantDialogFragment instanceof ConstantDialogFragment);
        assertTrue(constantDialogFragment.getShowsDialog());
    }

    @Test
    public void checkIfDataIsFetchedFromDialogAlertBox(){
        AlertDialog alertDialog = constantDialogFragment.getAlertDialog();
        if (alertDialog.isShowing()) {
            try {
                doClick(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void doClick(final Button button) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}


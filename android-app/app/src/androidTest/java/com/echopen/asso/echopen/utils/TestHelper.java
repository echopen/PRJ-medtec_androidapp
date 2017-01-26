package com.echopen.asso.echopen.utils;

import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Some help functions, refactoring and other utilities.
 */
public class TestHelper {

    public static void checkUiDoNotExist(List<Integer> uiID){
        for(int elem : uiID){
            onView(withId(elem)).check(doesNotExist());
        }
    }

    public static void checkUiIsDisplayed(List<Integer> uiID){
        for(int elem : uiID){
            onView(withId(elem)).check(matches(isDisplayed()));
        }
    }

    public static void checkUiIsVisible(List<Integer> uiID){
        for(int elem : uiID){
            onView(withId(elem)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
    }

    public static boolean clickOnDialog(View child, String string) {
        boolean thrown = false;
        child.performClick();
        try {
            onView(withText(string)).
                    perform(click());
        } catch (Exception e) {
            thrown = true;
        }
        return thrown;
    }

    public static void dismissTheAlertDialogBox() {
        onView(withText("Cancel")).
                perform(click());
    }
}

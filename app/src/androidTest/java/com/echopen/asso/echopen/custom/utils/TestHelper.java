package com.echopen.asso.echopen.custom.utils;

import android.support.test.espresso.ViewInteraction;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by mehdibenchoufi on 06/07/16.
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
}

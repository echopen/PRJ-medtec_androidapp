package com.echopen.asso.echopen.custom;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */
public class CustomActivityTest extends ActivityInstrumentationTestCase2<CustomActivity> {

    private CustomActivity mCustomActivityTest;

    public CustomActivityTest() {
        super(CustomActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCustomActivityTest = getActivity();
    }

    public void testPreconditions() {
        assertNotNull("mMainActivityTest is null", mCustomActivityTest);
    }
}

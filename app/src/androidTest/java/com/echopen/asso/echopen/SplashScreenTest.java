package com.echopen.asso.echopen;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */
public class SplashScreenTest extends ActivityInstrumentationTestCase2<SplashScreen> {

    private SplashScreen mSplashScreenTest;

    public SplashScreenTest() {
        super(SplashScreen.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSplashScreenTest = getActivity();
    }

    public void testPreconditions() {
        assertNotNull("mSplashScreenTest is null", mSplashScreenTest);
    }
}

package com.echopen.asso.echopen;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Basic activity test template @to be continued and documented here ;)
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

    public void Preconditions() {
        assertNotNull("mSplashScreenTest is null", mSplashScreenTest);
    }
}

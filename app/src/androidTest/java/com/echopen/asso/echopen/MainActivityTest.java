package com.echopen.asso.echopen;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivityTest;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivityTest = getActivity();
    }

    @Test
    public void testInitialAlwaysPasses(){
        assert true;
    }

    public void testPreconditions() {
        assertNull("mMainActivityTest is null", mMainActivityTest);
    }
}

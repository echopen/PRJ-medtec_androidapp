package com.echopen.asso.echopen;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Basic activity test template @to be continued and documented here ;)
 */
public class ShareTest extends ActivityInstrumentationTestCase2<Share> {

    private Share mShareTest;

    public ShareTest() {
        super(Share.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mShareTest = getActivity();
    }

    public void testPreconditions() {
        assertNotNull("mShareTest is null", mShareTest);
    }
}

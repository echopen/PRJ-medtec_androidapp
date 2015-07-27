package com.echopen.asso.echopen;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by mehdibenchoufi on 27/07/15.
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

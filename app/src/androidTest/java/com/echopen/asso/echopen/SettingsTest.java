package com.echopen.asso.echopen;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by mehdibenchoufi on 27/07/15.
 */
public class SettingsTest extends ActivityInstrumentationTestCase2<Settings> {

    private Settings mSettingsTest;

    public SettingsTest() {
        super(Settings.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSettingsTest = getActivity();
    }

    public void testPreconditions() {
        assertNotNull("mSettingsTest is null", mSettingsTest);
    }
}

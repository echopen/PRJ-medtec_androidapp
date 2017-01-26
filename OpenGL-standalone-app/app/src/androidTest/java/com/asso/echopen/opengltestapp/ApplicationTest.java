package com.asso.echopen.opengltestapp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private Application mApplication;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        createApplication();
        mApplication = getApplication();
    }


    public void testHasGLES20s(){
        Utils utils = new Utils();
        assertEquals(utils.checkHasGLES20(getContext()), true);
    }

}
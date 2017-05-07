package com.echopen.asso.echopen.ui;

import android.app.Activity;

/**
 * In order to separate concerns, View parts are handled by the *ActionController
 * Any Activity will extend AbstractActionActivity and
 * implement initActionController() method.
 * initActionController() will instantiate *ActionController that will inherit from
 * AbstractActionController
 */
public abstract class AbstractActionController {

    /* The Activity that extends AbstractActionActivity */
    private Activity activity;

    public AbstractActionController() {}

    public AbstractActionController(Activity activity) {}

    /* the Views make often use findViewById, this is abstracted here */
    public static void findViewById(){}

    /* some common type of Action that can be generically
    implemented across all Activities using this method */
    private void displayAction(){}
}

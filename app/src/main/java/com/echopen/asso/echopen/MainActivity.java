/*
 *
 */
package com.echopen.asso.echopen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.ui.CameraFragment;
import com.echopen.asso.echopen.ui.FilterFragment;
import com.echopen.asso.echopen.ui.MainActionController;


public class MainActivity extends CustomActivity implements AbstractActionActivity

{
    private static final int SWIPE_MIN_DISTANCE = 120;

    private static final int SWIPE_MAX_OFF_PATH = 250;

    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public static final int DISPLAY_VIDEO = 2;

    public static final int DISPLAY_FILTER = 1;

    public static final int DISPLAY_PHOTO = 0;

    private int display;

    private MainActionController mainActionController;

    public GestureDetector gesture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSwipeViews();
        initViewComponents();
        initActionController();
        setupContainer();
    }

    public void initActionController() {
        Activity activity = this;
        mainActionController = new MainActionController(activity);
    }


    private void initViewComponents() {
        setTouchNClick(R.id.btnCapture);
        setTouchNClick(R.id.btnEffect);
        setTouchNClick(R.id.btnPic);
        setTouchNClick(R.id.tabBrightness);
        setTouchNClick(R.id.tabGrid);
        setTouchNClick(R.id.tabSetting);
        setTouchNClick(R.id.tabSuffle);
        setTouchNClick(R.id.tabTime);

        setClick(R.id.btn1);
        setClick(R.id.btn2);
        setClick(R.id.btn3);
        setClick(R.id.btn4);
        setClick(R.id.btn5);

        applyBgTheme(findViewById(R.id.vTop));
        applyBgTheme(findViewById(R.id.vBottom));

    }

    private void initSwipeViews() {
        gesture = new GestureDetector(this, gestureListner);

        OnTouchListener otl = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        };
        findViewById(R.id.content_frame).setOnTouchListener(otl);
    }

    private SimpleOnGestureListener gestureListner = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(android.view.MotionEvent e1,
                               android.view.MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            else {
                try {
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        if (display != DISPLAY_VIDEO) {
                            display++;
                            setupContainer();
                        }

                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        if (display != DISPLAY_PHOTO) {
                            display--;
                            setupContainer();
                        }
                    }
                } catch (Exception e) {
                    // nothing
                }
                return true;
            }

        }
    };

    public void goBackFromFragment(int display) {
        this.display = display;
        setupContainer();
    }

    private void setupContainer() {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        Fragment f;
        if (display != DISPLAY_FILTER)
            f = new CameraFragment();
        else
            f = new FilterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, f).commit();

        if (display == DISPLAY_VIDEO) {
            mainActionController.displayVideo();
        } else {
            mainActionController.displayImages();

            if (display == DISPLAY_PHOTO) {
                mainActionController.displayPhoto();
            } else {
                mainActionController.displayOtherImage();
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (display != DISPLAY_PHOTO
                && (v.getId() == R.id.btnPic || v.getId() == R.id.btn1)) {
            display = DISPLAY_PHOTO;
            setupContainer();
        } else if (v.getId() == R.id.btn2) {
            if (display == DISPLAY_VIDEO) {
                display = DISPLAY_FILTER;
                setupContainer();
            } else if (display == DISPLAY_FILTER) {
                display = DISPLAY_PHOTO;
                setupContainer();
            }
        } else if (v.getId() == R.id.btn4) {
            if (display == DISPLAY_PHOTO) {
                display = DISPLAY_FILTER;
                setupContainer();
            } else if (display == DISPLAY_FILTER) {
                display = DISPLAY_VIDEO;
                setupContainer();
            }
        } else if (v.getId() == R.id.btn5 && display == DISPLAY_PHOTO) {
            display = DISPLAY_VIDEO;
            setupContainer();
        } else if (display != DISPLAY_FILTER && v.getId() == R.id.btnEffect) {
            display = DISPLAY_FILTER;
            setupContainer();
        } else if (v.getId() == R.id.btnCapture) {
            if (display == DISPLAY_FILTER) {
                display = DISPLAY_PHOTO;
                setupContainer();
            } else
                startActivity(new Intent(this, Share.class));
        } else if (v.getId() == R.id.tabSetting) {
            startActivity(new Intent(this, Settings.class));
        }
    }

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //
                            break;
                        case 1:
                            //
                        case 2:
                            //
                        case 3:
                            //
                    }
                }
            };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.btnCapture) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.choose_camera, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
        }

        return true;
    }
}

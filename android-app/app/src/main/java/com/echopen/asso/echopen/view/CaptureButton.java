package com.echopen.asso.echopen.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.MainFragment;

/**
 * Created by Yvan Moté on 28/02/2018.
 */

public class CaptureButton extends android.support.v7.widget.AppCompatImageButton {

    public interface CaptureButtonListener {
        void onTouchDown();
        void onTouchUp();
    }

    private RotateAnimation rotateAnimationCapture;
    private Long then;
    private CaptureButtonListener listener;

    private long animationDuration = 5000l;

    public CaptureButton(Context context) {
        super(context);
    }

    public CaptureButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CaptureButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(CaptureButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            then = System.currentTimeMillis();

            if(listener!=null) {
                listener.onTouchDown();
            }

            rotateAnimationCapture = new RotateAnimation(0,144 ,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.6f);
            rotateAnimationCapture.setDuration(animationDuration);

            this.clearAnimation();
            this.setAnimation(rotateAnimationCapture);

            ((MainActivity)getContext()).getMainFragment().longPressBegins();

            return true;
        } else if(event.getAction() == MotionEvent.ACTION_UP) {

            if(listener!=null) {
                listener.onTouchUp();
            }

            if(((Long) System.currentTimeMillis() - then) > animationDuration){
                Log.d("mcaptureButton", "Long Press");
                ((MainActivity)getContext()).getMainFragment().longPressAction();
                ((MainActivity)getContext()).getMainFragment().longPressCompleted();

                return true;
            }
            else {

                Log.d("mcaptureButton", "cancel");
                ((MainActivity)getContext()).getMainFragment().longPressInterrupted();

                rotateAnimationCapture.cancel();
                rotateAnimationCapture= null;

            }
        }
        return false;
    }
}

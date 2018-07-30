package com.echopen.asso.echopen.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.echopen.asso.echopen.R;

import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yvan Moté on 28/02/2018.
 */

public class CaptureButton extends android.support.v7.widget.AppCompatImageButton {

    public interface CaptureButtonListener {
        void onCancel();
        void onShortPress();
        void onLongPress();
    }

    private RotateAnimation rotateAnimationCapture;
    @BindView(R.id.main_button_shadow) ImageView mCaptureShadow;
    private Long then;
    private CaptureButtonListener listener;

    private Handler mHandler;
    private Runnable mLongPressRunnable;

    private long shortPressDuration = 500l;
    private long animationDuration = 5000l;

    public CaptureButton(Context context) {
        super(context);
        ButterKnife.bind(this);
    }

    public CaptureButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
    }

    public CaptureButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);
    }

    public void setListener(CaptureButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            then = System.currentTimeMillis();

            if(listener == null) {
                return true;
            }

            rotateAnimationCapture = new RotateAnimation(0,144 ,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.6f);
            rotateAnimationCapture.setDuration(animationDuration);

            this.clearAnimation();
            this.setAnimation(rotateAnimationCapture);

            mCaptureShadow.setImageResource(R.drawable.icon_arc_shadow);

            mLongPressRunnable = new Runnable() {
                @Override
                public void run() {
                    listener.onLongPress();
                    clearAnimation();
                    mCaptureShadow.setImageResource(R.drawable.icon_save_image);
                }
            };
            mHandler = new Handler();
            mHandler.postDelayed(mLongPressRunnable, animationDuration);

            return true;
        } else if(event.getAction() == MotionEvent.ACTION_UP) {

            if(listener == null) {
                return true;
            }

            Long currentTime = (Long)System.currentTimeMillis();

            if((currentTime - then) < shortPressDuration){
                listener.onShortPress();

                mHandler.removeCallbacks(mLongPressRunnable);
                rotateAnimationCapture.cancel();
                rotateAnimationCapture= null;

                mCaptureShadow.setImageResource(R.drawable.icon_save_image);

                return true;
            }
            else {

                listener.onCancel();

                mHandler.removeCallbacks(mLongPressRunnable);
                rotateAnimationCapture.cancel();
                rotateAnimationCapture= null;
                mCaptureShadow.setImageResource(R.drawable.icon_save_image);

                return false;
            }
        }
        return false;
    }
}

package com.echopen.asso.echopen.ui;

/**
 * Created by mehdibenchoufi on 28/06/16.
 *
 * Using and Refactoring code from
 * https://dhingrakimmi.wordpress.com/2015/09/17/android-measurment-scale-my-scale/?preview_id=2
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.model.Painter.SelfPaint;
import com.echopen.asso.echopen.model.Ruler.Ruler;
import com.echopen.asso.echopen.utils.Constants;

public class RulerView extends View {

    public Ruler ruler;

    public Ruler.Point point;

    float startingPoint = 0;

    float   downpoint = 0,
            movablePoint = 0,
            downPointClone = 0;

    boolean isDown = false;

    boolean isUpward = false;

    private boolean isMove;

    private onViewUpdateListener mListener;

    private Paint gradientPaint;

    private SelfPaint selfPaint = new SelfPaint(),
        textPaint = new SelfPaint(),
        goldenPaint = new SelfPaint();

    boolean isSizeChanged = false;

    float userStartingPoint = 0f;

    boolean isFirstTime = true;

    public SelfPaint getSelfPaint() {
        return selfPaint;
    }

    public RulerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ruler = new Ruler();
        point = ruler.getPoint();
        if (!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ruler.setRulerSize(ruler.pixelStep * 10);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");

        selfPaint.getBareInstance();
        selfPaint.setSelfStyle(Paint.Style.STROKE).setSelfStrokeWidth(0).
                setSelfAntiAlias(false).setSelfColor(Color.WHITE);

        textPaint.getTextInstance();
        textPaint.setSelfStyle(Paint.Style.STROKE).setSelfStrokeWidth(0).setSelfAntiAlias(true).
                setSelfTextSize(getResources().getDimension(R.dimen.txt_size)).
                setSelfColor(Color.WHITE).setTypeface(typeface);
    }

    public void setUpdateListener(onViewUpdateListener onViewUpdateListener) {
        mListener = onViewUpdateListener;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        ruler.setWidth(w);
        ruler.setHeight(h);
        ruler.setScreenSize(ruler.getHeight());
        ruler.pixelStep = ruler.getScreenSize() / Constants.RulerParam.step_decimation;
        point.setMidScreenPoint(ruler.getHeight() / 2);
        point.setEndPoint(ruler.getWidth() - 40);
        if (isSizeChanged) {
            isSizeChanged = false;
            float tmp = point.getMidScreenPoint() - (ruler.getScreenSize());
            point.setMainPoint(point.getMidScreenPoint() - 1100);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        int scaleLineSmall = (int) getResources().getDimension(R.dimen.scale_line_small),
                scaleLineMedium = (int) getResources().getDimension(R.dimen.scale_line_medium),
                scaleLineLarge = (int) getResources().getDimension(R.dimen.scale_line_large);
        int textStartPoint = (int) getResources().getDimension(R.dimen.text_start_point);
        int endPoint = point.getEndPoint();

        startingPoint = point.getMainPoint();
        for (int i = 1;; ++i) {
            if (startingPoint > ruler.getScreenSize()) {
                break;
            }
            startingPoint = startingPoint + ruler.pixelStep;
            int size = (i % 10 == 0) ? scaleLineLarge : (i % 5 == 0) ? scaleLineMedium : scaleLineSmall;
            canvas.drawLine(endPoint - size, startingPoint, endPoint, startingPoint, selfPaint.getSelfPainter());
            if (i % 10 == 0) {
                //canvas.drawText((i / 10) + " cm", endPoint - textStartPoint, startingPoint + 8, textPaint.getSelfPainter());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float mainPoint = point.getMainPoint();
        point.setMainPointClone(mainPoint);
        if (mainPoint < 0) {
            point.setMainPointClone(-mainPoint);
        }
        if (mListener != null) {
            mListener.onViewUpdate((point.getMidScreenPoint() + point.getMainPointClone()) / (ruler.pixelStep * 10));
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = true;
                isDown = false;
                isUpward = false;
                downpoint = event.getY();
                downPointClone = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                movablePoint = event.getY();
                if (downPointClone > movablePoint) {
                    Log.d("tagggy", "I am there in the first");
                    if (isUpward) {
                        downpoint = event.getY();
                        downPointClone = downpoint;
                    }
                    isDown = true;
                    isUpward = false;
                    Log.d("tagggy","downPointClone" + downPointClone + "movablePoint" + movablePoint);
                    if (downPointClone - movablePoint > 1) {
                        point.setMainPoint(mainPoint + (-(downPointClone - movablePoint)));
                        downPointClone = movablePoint;
                        invalidate();
                    }
                } else {
                    Log.d("tagggy", "I am there in the second");
                    if (isMove) {
                        if (isDown) {
                            downpoint = event.getY();
                            downPointClone = downpoint;
                        }
                        isDown = false;
                        isUpward = true;
                        Log.d("tagggy","movablePoint" + movablePoint + "downpoint" + downpoint);

                        if (movablePoint - downpoint > 1) {
                            point.setMainPoint(mainPoint + ((movablePoint - downPointClone)));
                            downPointClone = movablePoint;
                            if (point.getMainPoint() > 0) {
                                point.setMainPoint(0);
                                isMove = false;
                            }
                            invalidate();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("up");
            default:
                break;
        }
        return true;
    }

    public void setStartingPoint(float point) {
        userStartingPoint = point;
        isSizeChanged = true;
        if (isFirstTime) {
            isFirstTime = false;
            if (mListener != null) {
                mListener.onViewUpdate(point);
            }
        }
    }
}

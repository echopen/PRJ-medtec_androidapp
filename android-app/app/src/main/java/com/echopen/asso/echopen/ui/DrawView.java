package com.echopen.asso.echopen.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.model.Painter.SelfPaint;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.utils.MathOp;

;

/**
 * Created by mehdibenchoufi on 30/06/16.
 */
public class DrawView extends ImageView {

    /* Path that will be drawn*/
    private Path drawPath;
    /* Drawing and canvas paint inheriting of self returning methods of SelfPaint*/
    private SelfPaint drawPaint;

    private Paint canvasPaint;
    /* color choice */
    private int paintColor = Color.WHITE;
    /* Canvas on which hold Bitmap */
    private Canvas drawCanvas;
    /* Bitmap on which we draw lines */
    private Bitmap canvasBitmap;
    /* Painter size */
    private float painthSize;
    /* Starting point easting of the line drawing*/
    private float startX;
    /* Starting point northing of the line drawing */
    private float startY;
    /* Current easting position when dragging the line */
    private float currentX;
    /* Current northing position when dragging the line */
    private float currentY;
    /* Ending point easting of the line drawing*/
    private float endX;
    /* Ending point northing of the line drawing */
    private float endY;
    /* Mark point of draw starting */
    private boolean checkStartStatus;
    /* Mark point of draw starting */
    private boolean checkEndStatus;

    private Paint markerPaint;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupDrawing();
    }

    /**
     * setUp the drawing painter properties
     */
    private void setupDrawing() {
        painthSize = getResources().getInteger(R.integer.small_size);
        drawPath = new Path();
        drawPaint = new SelfPaint();
        drawPaint.getBareInstance();
        drawPaint.setSelfColor(paintColor).setSelfAntiAlias(true).
                setSelfStrokeWidth((int) painthSize).setSelfStyle(Paint.Style.STROKE);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        markerPaint = new Paint();
    }

    /**
     * Taking care of the size view
     * @param width
     * @param height
     * @param oldWidth, former width
     * @param oldHeight, former height
     */
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    /**
     * This is method is called after touch events
     * @param canvas
     */
    @Override
        protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(checkStartStatus){
            drawPaint.setSelfColor(Color.WHITE);
            markerPaint.setColor(Color.RED);
            markAndDraw(canvas, startX - 15, startY - 15, startX + 15, startY + 15,
                    markerPaint, drawPath, drawPaint.getSelfPainter());
        }
        else if (checkEndStatus) {
            drawPaint.setSelfColor(Color.RED);
            markAndDraw(canvas, endX - 15, endY - 15, endX + 15, endY + 15,
                    markerPaint, drawPath, drawPaint.getSelfPainter());
        }else {
            measureAndDisplayDistance(startX, currentX, startY, currentY);
            canvas.drawPath(drawPath, drawPaint.getSelfPainter());
        }
    }

    private void measureAndDisplayDistance(float X1, float X2, float Y1, float Y2) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float distance = MathOp.getRealMeasure((float) Math.hypot(X1 - X2, Y1 - Y2), displayMetrics);
        if(distance !=0)
            Synchronizer.singletonSynchronizer.synchronizeVisibility(R.id.measure);
        String measure = String.format("%.2f", distance);
        Synchronizer.singletonSynchronizer.synchronizeTextAndImage(R.id.measure, measure);
    }

    private void markAndDraw(Canvas canvas, float left, float top, float right, float bottom, Paint markerPaint, Path path, Paint paint ) {
        if(checkEndStatus)
            canvas.drawRect(startX - 15, startY - 15, startX + 15, startY + 15, markerPaint);
        canvas.drawRect(left, top, right, bottom, markerPaint);
        canvas.drawPath(path, paint);
    }

    /**
     * Clicking on the view should trigger a line to be drawn between this very clicked point
     * and the other one clicked. After this method's call, onDraw() method is called
     * @param event, MotionEvent
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkStartStatus = true;
                checkEndStatus = false;
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                checkStartStatus = false;
                currentX = event.getRawX();
                currentY = event.getRawY();
                drawPath.reset();
                drawPath.moveTo(startX, startY);
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                checkEndStatus = true;
                endX = touchX;
                endY = touchY;
                this.invalidate();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}


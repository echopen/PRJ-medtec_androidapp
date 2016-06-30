package com.echopen.asso.echopen.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.model.Data.AbstractDataTask;
import com.echopen.asso.echopen.model.Painter.SelfPaint;

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
    /* Brush size */
    private float brushSize;
    /* Starting point easting of the line drawing*/
    private float startX;
    /* Starting point northing of the line drawing*/
    private float startY;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupDrawing();
    }

    /**
     * setUp the drawing painter properties
     */
    private void setupDrawing() {
        brushSize = getResources().getInteger(R.integer.small_size);
        drawPath = new Path();
        drawPaint = new SelfPaint();
        drawPaint.getBareInstance();
        drawPaint.setSelfColor(paintColor).setSelfAntiAlias(true).
                setSelfStrokeWidth((int) brushSize).setSelfStyle(Paint.Style.STROKE);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
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
        canvas.drawPath(drawPath, drawPaint);
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
                startX = event.getRawX();
                startY = event.getRawY();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                Paint paint = new Paint();
                paint.setStrokeWidth(15f);
                paint.setColor(Color.BLACK);
                this.drawCanvas.drawLine(startX, startY, event.getRawX(), event.getRawY(), paint);
                this.invalidate();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}


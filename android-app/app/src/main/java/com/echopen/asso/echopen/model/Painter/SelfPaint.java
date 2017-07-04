package com.echopen.asso.echopen.model.Painter;

import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.text.TextPaint;

/**
 * Created by mehdibenchoufi on 28/06/16.
 *
 * A class aiming to make work together Paint and TexPaint instances
 * Implementing self-returning  methods in order to fold the code ;)
 */
public class SelfPaint extends Paint {

    private Paint selfPaint;

    public Paint getSelfPainter() {
        return selfPaint;
    }

    public SelfPaint() {
        super();
    }

    public Paint getBareInstance(){
        return selfPaint = new Paint();
    }

    public Paint getTextInstance(){
        return selfPaint = new TextPaint();
    }

    public SelfPaint setSelfStyle(Style style) {
        selfPaint.setStyle(style);
        return this;
    }

    public SelfPaint setSelfStrokeWidth(int v) {
        selfPaint.setStrokeWidth(v);
        return this;
    }

    public SelfPaint setSelfAntiAlias(boolean b) {
        selfPaint.setAntiAlias(b);
        return this;
    }

    public SelfPaint setSelfTextSize(float v) {
        selfPaint.setTextSize(v);
        return this;
    }

    public SelfPaint setSelfColor(int i) {
        selfPaint.setColor(i);
        return this;
    }

    public SelfPaint setSelfStrokeJoin(Join join) {
        selfPaint.setStrokeJoin(join);
        return this;
    }

    public SelfPaint setSelfStrokeCap(Cap cap) {
        selfPaint.setStrokeCap(cap);
        return this;
    }

    public PathEffect setSelfPathEffect(PathEffect effect) {
        return selfPaint.setPathEffect(effect);
    }

    public Typeface setTypeface(Typeface typeface) {
        return selfPaint.setTypeface(typeface);
    }
}

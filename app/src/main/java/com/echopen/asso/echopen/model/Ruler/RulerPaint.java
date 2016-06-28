package com.echopen.asso.echopen.model.Ruler;

import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.echopen.asso.echopen.ui.RulerView;

/**
 * Created by mehdibenchoufi on 28/06/16.
 *
 * A class aiming to make work together Paint and TexPaint instances
 * Implementing self-returning  methods in order to fold the code ;)
 */
public class RulerPaint extends Paint {

    private Paint rulerPainter;

    public Paint getRulerPainter() {
        return rulerPainter;
    }

    public RulerPaint() {
        super();
    }

    public Paint getBareInstance(){
        return rulerPainter = new Paint();
    }

    public Paint getTextInstance(){
        return rulerPainter = new TextPaint();
    }

    public RulerPaint setRulerStyle(Style style) {
        rulerPainter.setStyle(style);
        return this;
    }

    public RulerPaint setRulerStrokeWidth(int v) {
        rulerPainter.setStrokeWidth(v);
        return this;
    }

    public RulerPaint setRulerAntiAlias(boolean b) {
        rulerPainter.setAntiAlias(b);
        return this;
    }

    public RulerPaint setRulerTextSize(float v) {
        rulerPainter.setTextSize(v);
        return this;
    }

    public RulerPaint setRulerColor(int i) {
        rulerPainter.setColor(i);
        return this;
    }

    public RulerPaint setRulerStrokeJoin(Join join) {
        rulerPainter.setStrokeJoin(join);
        return this;
    }

    public RulerPaint setRulerStrokeCap(Cap cap) {
        rulerPainter.setStrokeCap(cap);
        return this;
    }

    public PathEffect setRulerPathEffect(PathEffect effect) {
        return rulerPainter.setPathEffect(effect);
    }

    public Typeface setTypeface(Typeface typeface) {
        return rulerPainter.setTypeface(typeface);
    }
}

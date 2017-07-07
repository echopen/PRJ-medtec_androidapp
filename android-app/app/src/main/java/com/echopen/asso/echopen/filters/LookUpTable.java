package com.echopen.asso.echopen.filters;

/*!
  An abstract look up table class
 */

enum LutType{
    LinearLut,
    ExponentialLut,
}

public abstract class LookUpTable{
    protected LutType mLutType;

    // pixels are stored on 4x8bits channels (R - G - B + Alpha channels)
    protected final int MIN_PIXEL_VALUE = 0;
    protected final int MAX_PIXEL_VALUE = 255;

    // intensity are included in a 16 bit range
    protected final int MIN_INTENSITY_VALUE = 0;
    protected final int MAX_INTENSITY_VALUE = 65535;

    public void computeLut(){}

    public abstract int applyLut(int iPixelIntensity);
}


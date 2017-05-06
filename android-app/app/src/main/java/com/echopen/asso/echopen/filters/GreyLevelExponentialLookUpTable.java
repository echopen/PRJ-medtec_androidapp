package com.echopen.asso.echopen.filters;

import android.util.Log;

/*!
  GreyLevelExponentialLookUpTable is an exponential look up table that converts a 8 bits intensity pixels to a RGB color pixel
 */
public class GreyLevelExponentialLookUpTable extends LookUpTable{

    protected double mAlpha;
    private final String TAG = this.getClass().getSimpleName();

    public GreyLevelExponentialLookUpTable(double alpha) {
        this.mLutType = LutType.ExponentialLut;
        this.mAlpha = alpha;
    }

    public int applyLut(int iPixelIntensity){
        int iExp = (int) (Math.exp(mAlpha * iPixelIntensity) - 1);

        if(iExp > MAX_PIXEL_VALUE){
            iExp = MAX_PIXEL_VALUE;
        }

        return iExp| iExp << 8 | iExp << 16 | 0xFF000000;
    }
}

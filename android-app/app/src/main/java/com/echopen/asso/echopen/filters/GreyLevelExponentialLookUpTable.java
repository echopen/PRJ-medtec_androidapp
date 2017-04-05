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

    public void computeLut(){
        int lPixelIntensityRange = MAX_INTENSITY_VALUE + 1 - MIN_INTENSITY_VALUE;
        mData = new int[lPixelIntensityRange];

        for(int i = 0; i < lPixelIntensityRange; i++){
            int iExp = (int) (Math.exp(mAlpha * i) - 1);

            if(iExp > MAX_PIXEL_VALUE){
                iExp = MAX_PIXEL_VALUE;
            }

            mData[i] = iExp| iExp << 8 | iExp << 16 | 0xFF000000;
        }
    }
}

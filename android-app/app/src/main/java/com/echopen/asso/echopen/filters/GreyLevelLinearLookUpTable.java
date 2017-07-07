package com.echopen.asso.echopen.filters;

/*!
  GreyLevelLinearLookUpTable is a linear look up table that converts a 8 bits intensity pixels to a RGB color pixel
 */

public class GreyLevelLinearLookUpTable extends LookUpTable{

    protected double mOffset;
    protected double mSlope;

    public GreyLevelLinearLookUpTable(double offset, double slope) {
        this.mLutType = LutType.LinearLut;
        this.mOffset = offset;
        this.mSlope = slope;
    }

    public int applyLut(int iPixelIntensity){
        int iLin = (int) (iPixelIntensity * mSlope + mOffset);

        if(iLin < MIN_PIXEL_VALUE){
            iLin = MIN_PIXEL_VALUE;
        }
        else if(iLin > MAX_PIXEL_VALUE){
            iLin = MAX_PIXEL_VALUE;
        }
        return iLin | iLin << 8 | iLin << 16 | 0xFF000000;
    }

    public double getOffset(){
        return mOffset;
    }

    public double getSlope(){
        return mSlope;
    }
}

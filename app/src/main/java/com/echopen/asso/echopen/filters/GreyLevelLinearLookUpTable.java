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

    public void computeLut(){
        int lPixelIntensityRange = (MAX_INTENSITY_VALUE + 1) - MIN_INTENSITY_VALUE;
        mData = new int[lPixelIntensityRange];

        for(int i = 0; i < lPixelIntensityRange; i++){
            int iLin = (int) (i * mSlope + mOffset);

            if(iLin < MIN_PIXEL_VALUE){
                iLin = MIN_PIXEL_VALUE;
            }
            else if(iLin > MAX_PIXEL_VALUE){
                iLin = MAX_PIXEL_VALUE;
            }
            mData[i] = iLin | iLin << 8 | iLin << 16 | 0xFF000000;
        }
    }

    public double getOffset(){
        return mOffset;
    }

    public double getSlope(){
        return mSlope;
    }
}

package com.echopen.asso.echopen.filters;

/*!
  RenderingContext class stores all rendering parameters that can be modified on the fly by the user
 */

public class RenderingContext {
    private LookUpTable mLookUpTable;
    private double mIntensityGain;

    public static final double DEFAULT_LUT_OFFSET = -6.0;
    public static final double DEFAULT_LUT_SLOPE = 2.30;

    public RenderingContext(){
        this.mIntensityGain = 0;
        this.mLookUpTable = new GreyLevelLinearLookUpTable(0.0, 1.0);
    }

    public RenderingContext(RenderingContext iRenderingContext) {
        this.mLookUpTable = iRenderingContext.mLookUpTable;
        this.mIntensityGain = iRenderingContext.mIntensityGain;
    }

    public void setIntensityGain(double iIntensityGain){
        this.mIntensityGain = iIntensityGain;
    }

    public void setLookUpTable(LookUpTable iLookUpTable){
        this.mLookUpTable = iLookUpTable;
    }

    public LookUpTable getLookUpTable(){
        return mLookUpTable;
    }

    public double getIntensityGain(){
        return mIntensityGain;
    }
}

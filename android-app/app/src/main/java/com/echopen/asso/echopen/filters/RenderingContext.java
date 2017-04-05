package com.echopen.asso.echopen.filters;

/*!
  RenderingContext class stores all rendering parameters that can be modified on the fly by the user
 */

public class RenderingContext {
    private LookUpTable mLookUpTable;
    private double mIntensityGain;

    public RenderingContext(){
        this.mIntensityGain = 0;
        this.mLookUpTable = new GreyLevelLinearLookUpTable(0, 1);
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

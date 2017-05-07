package com.echopen.asso.echopen.ui;

import com.echopen.asso.echopen.filters.GreyLevelExponentialLookUpTable;
import com.echopen.asso.echopen.filters.GreyLevelLinearLookUpTable;
import com.echopen.asso.echopen.filters.LookUpTable;
import com.echopen.asso.echopen.filters.RenderingContext;

/*!
  RenderingContextController is the class used to edit the RenderingContext following a user action.
  Class thread safe implementation allows an interaction between user image manipulation and sensor data streaming
  */

public class RenderingContextController {
    private RenderingContext mRenderingContext;

    public RenderingContextController() {
            this.mRenderingContext = new RenderingContext();
    }

    public synchronized RenderingContext getCurrentRenderingContext(){
        return new RenderingContext(mRenderingContext);
    }

    public synchronized void setExponentialLutAlpha(double iAlpha){
        this.mRenderingContext.setLookUpTable(new GreyLevelExponentialLookUpTable(iAlpha));
    }

    public synchronized void setLinearLutOffset(double iOffset){
        LookUpTable lCurrentLookUpTable = this.mRenderingContext.getLookUpTable();
        double lCurrentSlope = 1.0;

        if(lCurrentLookUpTable instanceof GreyLevelLinearLookUpTable) {
            GreyLevelLinearLookUpTable lCurrentLinearLookUpTable = (GreyLevelLinearLookUpTable) lCurrentLookUpTable;
            lCurrentSlope = lCurrentLinearLookUpTable.getSlope();
        }
        this.mRenderingContext.setLookUpTable(new GreyLevelLinearLookUpTable(iOffset, lCurrentSlope));
    }

    public synchronized void setLinearLutSlope(double iSlope){
        LookUpTable lCurrentLookUpTable = this.mRenderingContext.getLookUpTable();

        double lCurrentOffset = 0.0;
        if(lCurrentLookUpTable instanceof GreyLevelLinearLookUpTable) {
            GreyLevelLinearLookUpTable lCurrentLinearLookUpTable = (GreyLevelLinearLookUpTable) lCurrentLookUpTable;
            lCurrentOffset = lCurrentLinearLookUpTable.getOffset();
        }
        this.mRenderingContext.setLookUpTable(new GreyLevelLinearLookUpTable(lCurrentOffset, iSlope));
    }

    public synchronized void setIntensityGain(double iGain){
        this.mRenderingContext.setIntensityGain(iGain);
    }
}

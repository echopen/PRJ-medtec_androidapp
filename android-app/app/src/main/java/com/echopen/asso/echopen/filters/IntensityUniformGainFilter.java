package com.echopen.asso.echopen.filters;

/*!
  IntensityUniformGainFilter is class that takes an input image with pixel intensity on a 8bits range and apply
  on it an uniform gain in dB

  Non optimized implementation - to switch on ITK open source framework
 */

import com.echopen.asso.echopen.utils.Constants;

public class IntensityUniformGainFilter {

    private int[] mImageInput;

    private int[] mImageOutput;

    private final String TAG = this.getClass().getSimpleName();

    public void setImageInput(int[] iImageInput){
        mImageInput = iImageInput;
    }


    public Boolean applyFilter(double iPowerGain){

        // convert dB to power factor
        double lPowerRatio = Math.pow(10, 0.1 * iPowerGain);

        mImageOutput = new int[mImageInput.length];
        for(int i = 0; i < mImageInput.length; i++){
            mImageOutput[i] = (int) (mImageInput[i] * lPowerRatio);

            if(mImageOutput[i] < Constants.PreProcParam.MIN_INTENSITY_PIXEL_VALUE){
                mImageOutput[i] = Constants.PreProcParam.MIN_INTENSITY_PIXEL_VALUE;
            }

            if(mImageOutput[i] > Constants.PreProcParam.MAX_INTENSITY_PIXEL_VALUE){
                mImageOutput[i] = Constants.PreProcParam.MAX_INTENSITY_PIXEL_VALUE;
            }
        }

        return true;
    }

    public int[] getImageOutput(){
        return mImageOutput;
    }
}

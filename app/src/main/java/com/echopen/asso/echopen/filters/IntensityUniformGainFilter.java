package com.echopen.asso.echopen.filters;

/*!
  IntensityUniformGainFilter is class that takes an input image with pixel intensity on a 8bits range and apply
  on it an uniform gain in dB

  Non optimized implementation - to switch on ITK open source framework
 */

public class IntensityUniformGainFilter {

    private final int MIN_INTENSITY_VALUE = 0;
    private final int MAX_INTENSITY_VALUE = 255;
    private int[] mImageInput;
    private int mImageInputSize;

    private int[] mImageOutput;

    private final String TAG = this.getClass().getSimpleName();

    public void setImageInput(int[] iImageInput, int iImageInputSize){
        mImageInput = iImageInput;
        mImageInputSize = iImageInputSize;
    }


    public Boolean applyFilter(double iPowerGain){

        // convert dB to power factor
        double lPowerRatio = Math.pow(10, 0.1 * iPowerGain);

        mImageOutput = new int[mImageInputSize];
        for(int i = 0; i < mImageInputSize; i++){
            mImageOutput[i] = (int) (mImageInput[i] * lPowerRatio);

            if(mImageOutput[i] < MIN_INTENSITY_VALUE){
                mImageOutput[i] = MIN_INTENSITY_VALUE;
            }

            if(mImageOutput[i] > MAX_INTENSITY_VALUE){
                mImageOutput[i] = MAX_INTENSITY_VALUE;
            }
        }

        return true;
    }

    public int[] getImageOutput(){
        return mImageOutput;
    }
}

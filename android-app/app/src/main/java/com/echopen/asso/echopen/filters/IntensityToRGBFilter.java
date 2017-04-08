package com.echopen.asso.echopen.filters;

/*!
  IntensityToRGBFilter is class that takes an input image with pixel intensity on a 8bits range and apply a color LUT
  to convert it to an RGB image

    Non optimized implementation - to switch on ITK open source framework
 */

public class IntensityToRGBFilter {

    private final String TAG = this.getClass().getSimpleName();
    private int[] mImageInput;

    private int[] mImageOutput;

    public void setImageInput(int[] iImageInput){
        mImageInput = iImageInput;
    }

    public Boolean applyFilter( LookUpTable iLUT){
        mImageOutput = new int[mImageInput.length];

        for(int i = 0; i < mImageInput.length; i++){
            mImageOutput[i] = iLUT.applyLut(mImageInput[i]);
        }

        return true;
    }

    public int[] getImageOutput(){
        return mImageOutput;
    }
}

package com.echopen.asso.echopen.filters;

/*!
  IntensityToRGBFilter is class that takes an input image with pixel intensity on a 8bits range and apply a color LUT
  to convert it to an RGB image

    Non optimized implementation - to switch on ITK open source framework
 */

public class IntensityToRGBFilter {

    private final String TAG = this.getClass().getSimpleName();
    private int[] mImageInput;
    private int mImageInputSize;

    private int[] mImageOutput;

    public void setImageInput(int[] iImageInput, int iImageInputSize){
        mImageInput = iImageInput;
        mImageInputSize = iImageInputSize;
    }

    public Boolean applyFilter( LookUpTable iLUT){

        iLUT.computeLut();
        int[] lLUTData = iLUT.getLutData();

        mImageOutput = new int[mImageInputSize];
        for(int i = 0; i < mImageInputSize; i++){
            mImageOutput[i] = lLUTData[mImageInput[i]];
        }

        return true;
    }

    public int[] getImageOutput(){
        return mImageOutput;
    }
}

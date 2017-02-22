package com.echopen.asso.echopen.filters;


public class EnvelopDetectionFilter {
    private Integer[] mImageInput;

    private Integer mSamplesPerLine;
    private Integer mLinesPerImage;

    private Integer[] mImageOutput;

    public void setImageInput(Integer[] iImageInput, Integer iSamplesPerLine, Integer iLinesPerImage){
        mImageInput = iImageInput;
        mSamplesPerLine = iSamplesPerLine;
        mLinesPerImage = iLinesPerImage;
    }

    public Boolean applyFilter(){

        double[] lImageInputOnDouble = new double[mImageInput.length];

        //store 16 bits values on double
        for(Integer i = 0; i < mImageInput.length; i++){
            lImageInputOnDouble[i] = mImageInput[i].doubleValue();
        }

        for(Integer i = 0; i< mLinesPerImage; i++){
            for(Integer j = 0; j<mSamplesPerLine; j++){
                //TODO: add correct import and process line by line
                /*FastFourierTransform.Complex[] lComplexInput = new FastFourierTransform.Complex[lImageInputOnDouble.length];
                for (int i = 0; i < lImageInputOnDouble.length; i++)
                    lComplexInput[i] = new FastFourierTransform.Complex(lImageInputOnDouble[i], 0.0);
                FastFourierTransform.fft(lComplexInput);*/

                //TODO: implement pass band filtering
                //FastFourierTransform.i_fft(lComplexInput);

                //TODO: store result on image output
            }
        }


        return true;
    }

    public Integer[] getImageOutput(){
        return mImageOutput;
    }

}

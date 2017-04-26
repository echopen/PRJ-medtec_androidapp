package com.echopen.asso.echopen.filters;


import android.util.Log;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;

import static com.echopen.asso.echopen.utils.Constants.PreProcParam.TCP_IMG_DATA;
import static com.echopen.asso.echopen.utils.Constants.PreProcParam.TCP_NUM_SAMPLES;
import static java.lang.Math.sqrt;

public class EnvelopDetectionFilter {
    private Integer[] mImageInput;

    private Integer mSamplesPerLine;
    private Integer mLinesPerImage;

    private Integer[] mlineImageOutput;
    private Integer[] mImageOutput;

    public void setImageInput(Integer[] iImageInput, Integer iSamplesPerLine, Integer iLinesPerImage){
        mImageInput = iImageInput;
        mImageOutput = new Integer[TCP_NUM_SAMPLES*TCP_IMG_DATA];
        mlineImageOutput = new Integer[TCP_NUM_SAMPLES];
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
                //TODO: add correct import and process line by line
                /*FastFourierTransform.Complex[] lComplexInput = new FastFourierTransform.Complex[TCP_NUM_SAMPLES];

                for (int j = 0; j < TCP_NUM_SAMPLES; j++)
                    lComplexInput[j] = new FastFourierTransform.Complex(lImageInputOnDouble[454+i*mSamplesPerLine+j], 0.0);
                FastFourierTransform.fft(lComplexInput);

                for (int j = 0; j < 65; j++) {
                    lComplexInput[j] = new FastFourierTransform.Complex(0.0, 0.0);
                }

                for (int j = 393; j < 1024; j++) {
                    lComplexInput[j] = new FastFourierTransform.Complex(0.0, 0.0);
                }

                FastFourierTransform.i_fft(lComplexInput);

                for (int k = 0; k < TCP_NUM_SAMPLES; k++) {
                    mlineImageOutput[k] = Integer.valueOf((int)(sqrt(Math.pow(lComplexInput[k].re,2) + (int) Math.pow(lComplexInput[k].im,2)))/TCP_NUM_SAMPLES);
                }*/

            System.arraycopy(lImageInputOnDouble, i * Constants.PreProcParam.NUM_SAMPLES_PER_LINE, mImageOutput, i*TCP_NUM_SAMPLES , TCP_NUM_SAMPLES);
        }
        return true;
    }

    public Integer[] getImageOutput(){
        return mImageOutput;
    }

}

package com.echopen.asso.echopen.filters;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;

import com.android.rs.ScriptC_fft;
import com.android.rs.ScriptC_ifft;
import com.echopen.asso.echopen.utils.Constants;

import java.util.Arrays;

/**
 * Created by lecoucl on 29/03/17.
 */
public class EnvelopeDetectionRenderscriptFilter {
    private final String TAG = this.getClass().getSimpleName();
    private int[] mImageInput;
    private int mNbSamplesPerLine;
    private int mNbLinesPerImage;
    private int[] mImageOutput;

    private int mNbOutputSamplesPerLine;

    // TODO: document filter inputs value
    private int mBandPassFilterLowerCutoffIndex = 0;
    private int mBandPassFilterUpperCutoffIndex = 0;

    // renderscript context
    private static Boolean isRenderscriptContextInitialized = false;
    private static RenderscriptContext mRenderscriptContext;

    public void setImageInput(int[] iImageInput, int iNbSamplesPerLine, int iNbLinesPerImage){
        mImageInput = iImageInput;
        mNbSamplesPerLine = iNbSamplesPerLine;
        mNbLinesPerImage = iNbLinesPerImage;

        mNbOutputSamplesPerLine = 0;
    }

    public int[] getImageOutput(){
        return mImageOutput;
    }

    public Boolean applyFilter(RenderScript iRenderscript, int iNbOutputSamplesPerLine){
        mNbOutputSamplesPerLine = iNbOutputSamplesPerLine;
        mBandPassFilterLowerCutoffIndex = getCutoffFrequencyIndex(Constants.PreProcParam.BAND_PASS_FILTER_LOWER_CUTOFF_FREQUENCY, Constants.PreProcParam.SAMPLING_FREQUENCY_BIS, mNbOutputSamplesPerLine);
        mBandPassFilterUpperCutoffIndex = getCutoffFrequencyIndex(Constants.PreProcParam.BAND_PASS_FILTER_UPPER_CUTOFF_FREQUENCY, Constants.PreProcParam.SAMPLING_FREQUENCY_BIS, mNbOutputSamplesPerLine);

        prepareRenderscriptContext(iRenderscript);

        mImageOutput = mRenderscriptContext.run(mImageInput);

        return true;
    }

    private void prepareRenderscriptContext(RenderScript iRenderscript){
        if(!isRenderscriptContextInitialized){
            mRenderscriptContext = new RenderscriptContext(iRenderscript);
            isRenderscriptContextInitialized = true;
        }
    }

    private class RenderscriptContext {
        private RenderScript mRenderscript;

        private ScriptC_fft mScriptCFft;
        private ScriptC_ifft mScriptCiFft;

        private Allocation mRsLineInput;
        private Allocation mRsLineOutput;
        private Allocation mTmp;

        public RenderscriptContext(RenderScript iRenderscript){
            mRenderscript = iRenderscript;
            mScriptCFft = new ScriptC_fft(mRenderscript);
            mScriptCiFft = new ScriptC_ifft(mRenderscript);

            mRsLineInput = Allocation.createSized(mRenderscript, Element.F32_2(mRenderscript), mNbOutputSamplesPerLine);
            mTmp = Allocation.createTyped(mRenderscript, mRsLineInput.getType());
            mRsLineOutput = Allocation.createTyped(mRenderscript, mRsLineInput.getType());;
            isRenderscriptContextInitialized = true;
        }

        public int[] run(int[] iImageInput){
            int[] oImageOutput = new int[mNbOutputSamplesPerLine * mNbLinesPerImage];
            int[] lLineData = new int[mNbOutputSamplesPerLine];
            for(int i = 0; i < mNbLinesPerImage; i++){
                // Be careful a crop on data is done to fit "power of two" array size required by FFT algorithm

                Arrays.fill(lLineData, 0);
                System.arraycopy(iImageInput, mNbSamplesPerLine * i, lLineData, 0, mNbSamplesPerLine);

                int[] oLineData = runOnALine(lLineData);
                System.arraycopy(oLineData, 0, oImageOutput, i * oLineData.length, oLineData.length);
            }

            return oImageOutput;
        }

        public int[] runOnALine(int [] iLineData){

            // format data to get Re(ImageInput[0]) Im(ImageInput[0]) Re(ImageInput[1]) Im(ImageInput[1]) Re(ImageInput[2]) Im(ImageInput[2]) ...
            float[] lFormattedData = new float[iLineData.length * 2];
            for(int i = 0; i < iLineData.length; i++){
                lFormattedData[2 * i] = (float)iLineData[i];
                lFormattedData[2 * i + 1] = 0;
            }

            float[] lOutputData = new float[iLineData.length * 2];
            mRsLineInput.copyFrom(lFormattedData);

            mScriptCFft.invoke_runRestricted(mScriptCFft, mRsLineInput, mTmp);
            float[] lTmpLineData = new float[iLineData.length * 2];

            mRsLineInput.copyTo(lTmpLineData);
            for(int i = 0; i < mBandPassFilterLowerCutoffIndex; i++){
                lTmpLineData[2*i] = 0;
                lTmpLineData[2 *i + 1] = 0;
            }

            for(int i = mBandPassFilterUpperCutoffIndex; i < iLineData.length; i++){
                lTmpLineData[2*i] = 0;
                lTmpLineData[2 *i + 1] = 0;
            }

            mTmp.copyFrom(lTmpLineData);

            mScriptCiFft.invoke_inverseRunRestricted(mScriptCiFft, mTmp, mRsLineOutput, iLineData.length);
            mRsLineOutput.copyTo(lOutputData);

            int[] oLineData = new int[iLineData.length];

            for(int i = 0; i < oLineData.length;i++){
                oLineData[i] = (int)Math.sqrt( (lOutputData[2*i] * lOutputData[2*i] + lOutputData[2*i + 1] * lOutputData[2*i + 1]) );
            }

            return oLineData;
        }
    }

    /**
     * @brief method return the discrete fourier transform sample index corresponding to a cutoff frequency
     *
     * @param iCutoffFrequency input cutoff frequency in Hz
     * @param iSamplingFrequency input signal sampling frequency in Hz
     * @param iNbSamples number of samples used for the FFT
     *
     *@return discrete fourier transform sample index
     */
    private int getCutoffFrequencyIndex(double iCutoffFrequency, double iSamplingFrequency, int iNbSamples){
        return (int) (iCutoffFrequency * iNbSamples / iSamplingFrequency);
    }

}

package com.echopen.asso.echopen.filters;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;

import com.echopen.asso.echopen.gpuchallenge.ScriptC_scanConversion;
import com.echopen.asso.echopen.model.Data.DeviceConfiguration;
import com.echopen.asso.echopen.utils.Constants;

/**
 * Created by lecoucl on 28/03/17.
 */
public class ScanConversionRenderscriptFilter {
    private int[] mImageInput;
    private int[] mImageOutput;

    //scan conversion context
    private static Boolean mIsScanConversionContextInitialized = false;
    private static ScanConversionContext mScanConversionContext;

    // renderscript context
    private static Boolean isRenderscriptContextInitialized = false;
    private static RenderscriptContext mRenderscriptContext;

    public ScanConversionRenderscriptFilter(){
    }

    public void setImageInput(int[] iImageInput){
        this.mImageInput = iImageInput;
    }

    public int[] getImageOutput(){
        return mImageOutput;
    }

    public Boolean applyFilter(RenderScript iRenderscript, DeviceConfiguration iDeviceConfiguration){
        mImageOutput = new int[Constants.PreProcParam.N_x * Constants.PreProcParam.N_z];

        this.prepareScanConversionContext(iDeviceConfiguration);

        this.prepareRenderscriptContext(iRenderscript);

        this.run();
        return true;
    }

    public void prepareScanConversionContext(DeviceConfiguration iDeviceConfiguration) {

        if(!mIsScanConversionContextInitialized){
            mScanConversionContext = new ScanConversionContext(iDeviceConfiguration);
            mIsScanConversionContextInitialized = true;
        }

    }

    private void prepareRenderscriptContext(RenderScript iRenderscript){
        if(!isRenderscriptContextInitialized){
            mRenderscriptContext = new RenderscriptContext(iRenderscript, mScanConversionContext);
            isRenderscriptContextInitialized = true;
        }

    }

    private void run(){
        mRenderscriptContext.mRsInputImage.copyFrom(mImageInput);
        mRenderscriptContext.mScriptCScanConversion.bind_envelope_data(mRenderscriptContext.mRsInputImage);

        mRenderscriptContext.mScriptCScanConversion.forEach_scan_convert(mRenderscriptContext.mRsIndexCounter);

        mRenderscriptContext.mScriptCScanConversion.get_output_image().copyTo(mImageOutput);
    }


    private class ScanConversionContext{
        private int[] mIndexData;
        private int[] mIndexImage;
        private double[] mWeight;
        private int[] mIndexCounter;
        private int mNbSamplesPerLines;

        public ScanConversionContext(DeviceConfiguration iDeviceConfiguration){
            computeTables(iDeviceConfiguration);
        }

        public void computeTables(DeviceConfiguration iDeviceConfiguration) {
            double lProbeAngleRad = iDeviceConfiguration.getProbeSectorAngle() * Math.PI /180;

            double start_depth = iDeviceConfiguration.getR0() * Math.cos(lProbeAngleRad / 2.0); /*  Depth for start of image in meters    */
            double image_size = (iDeviceConfiguration.getRf() - start_depth);  /*  Size of image in meters               */

            double start_of_data = iDeviceConfiguration.getR0();   /*  Depth for start of data in meters     */

            int N_samples  = (int) Math.floor(Constants.PreProcParam.TCP_NUM_SAMPLES);       /*  Number of data samples                */
            double delta_r = image_size / N_samples; /*  Sampling interval for data in meters  */

            int N_lines = iDeviceConfiguration.getNbLinesPerImage();    /*  Number of acquired lines              */

            double delta_theta = lProbeAngleRad / N_lines ; /*  Angle between individual lines        */
            double theta_start = lProbeAngleRad / 2;    /*  Angle for first line in image         */

            int Nz = Constants.PreProcParam.N_z;              /*  Size of image in pixels               */
            int Nx = Constants.PreProcParam.N_x;              /*  Size of image in pixels               */

            mNbSamplesPerLines = Constants.PreProcParam.TCP_NUM_SAMPLES;
            int Ncoef_max = 4;
            double weight_coef[] = new double[Nz * Nx * Ncoef_max];    //*  The weight table                      *//*
            int index_samp_line[] = new int[Nz * Nx];  //Index for the data sample number
            int image_index[] = new int[Nz * Nx];

            make_tables(start_depth, image_size, start_of_data, delta_r, N_samples, theta_start, -delta_theta, N_lines, Nz, Nx, weight_coef, index_samp_line, image_index);
            // TODO convert arrays to fields.*/
        }

        /**
         * make_tables() compute the weights affected to each 4-uplets of pixels.
         * This table is calculated only once in the whole process, since it onmy dependes of physical
         * constants that are determined by the hardware and the properties of the mobile device
         * @param start_depth
         * @param image_size
         * @param start_of_data
         * @param delta_r
         * @param N_samples
         * @param theta_start
         * @param delta_theta
         * @param N_lines
         * @param Nz
         * @param Nx
         * @param weight_coef
         * @param index_samp_line
         * @param image_index
         * @return N_values Number of values to calculate in the image
         */
        private void make_tables(double start_depth,     /*  Depth for start of image in meters    */
                                 double image_size,      /*  Size of image in meters               */

                                 double start_of_data,   /*  Depth for start of data in meters     */
                                 double delta_r,         /*  Sampling interval for data in meters  */
                                 int N_samples,       /*  Number of data samples                */

                                 double theta_start,     /*  Angle for first line in image         */
                                 double delta_theta,     /*  Angle between individual lines        */
                                 int N_lines,         /*  Number of acquired lines              */
                                 int Nz,              /*  Size of image in pixels               */
                                 int Nx,              /*  Size of image in pixels               */


                                 double weight_coef[],    /*  The weight table                      */
                                 int index_samp_line[],/*  Index for the data sample number      */
                                 int image_index[]    /*  Index for the image matrix            */
        ) {
            int N_values;
            int i, j;            /*  Integer loop counters                      */

            double z, z2, x;         /*  Image coordinates in meters                */
            double dz, dx;          /*  Increments in image coordinates in meters  */
            double radius;         /*  Radial distance                            */
            double theta;          /*  Angle in degrees                           */

            double samp;           /*  Sample number for interpolation            */
            double line;           /*  Line number for interpolation              */
            int index_samp;     /*  Index for the data sample number           */
            int index_line;     /*  Index for the data line number             */
            double samp_val;       /*  Sub-sample fraction for interpolation      */
            double line_val;       /*  Sub-line fraction for interpolation        */

            boolean make_pixel;     /*  Whether the values is used in the image    */
            int ij_index;       /*  Index into array                           */
            int ij_index_coef;  /*  Index into coefficient array               */


            dz = image_size / Nz;
            dx = image_size / Nx;
            z = start_depth;
            ij_index = 0;
            ij_index_coef = 0;

            for (i = 0; i < Nz; i++) {
                x = -image_size / 2;
                z2 = z * z;

                for (j = 0; j < Nx; j++) {
            /*  Find which samples to select from the envelope array  */

                    radius = Math.sqrt(z2 + x * x);
                    theta = Math.atan2(x, z);
                    samp = (radius - start_of_data) / delta_r;
                    line = (theta - theta_start) / delta_theta;
                    index_samp = (int)Math.floor(samp);
                    index_line = (int)Math.floor(line);

                /*  Test whether the samples are outside the array        */

                    make_pixel = (index_samp >= 0) && (index_samp + 1 < N_samples)
                            && (index_line >= 0) && (index_line + 1 < N_lines);

                    if (make_pixel) {
                        samp_val = samp - index_samp;
                        line_val = line - index_line;
                /*  Calculate the coefficients if necessary   */
                        weight_coef[ij_index_coef] = (1 - samp_val) * (1 - line_val);
                        weight_coef[ij_index_coef + 1] = samp_val * (1 - line_val);
                        weight_coef[ij_index_coef + 2] = (1 - samp_val) * line_val;
                        weight_coef[ij_index_coef + 3] = samp_val * line_val;

                        index_samp_line[ij_index] = index_samp + index_line * N_samples;
                        image_index[ij_index] = (Nx - j - 1) * Nz + i;

                        ij_index++;
                        ij_index_coef = ij_index_coef + 4;
                    }
                    x = x + dx;
                }
                z = z + dz;
            }

            N_values = ij_index;
            //System.out.println("Table has now been set-up, %d x %d, %d %d values\n",Nz,Nx,ij_index, N_values);
            // as for a test purpose
            this.mIndexData = index_samp_line;
            this.mIndexImage = image_index;
            this.mWeight =  weight_coef;
            this.mIndexCounter = new int[N_values];
            for (i = 0; i < N_values;i++) {
                this.mIndexCounter[i] = i;
            }

        }

    }

    private class RenderscriptContext{
        private RenderScript mRenderscript;
        private ScriptC_scanConversion mScriptCScanConversion;

        private Allocation mRsInputImage;
        private Allocation mRsOutputImage;
        private Allocation mRsIndexSampleLine;
        private Allocation mRsIndexCounter;
        private Allocation mRsImageIndex;
        private Allocation mRsWeight;

        RenderscriptContext(RenderScript iRenderscript, ScanConversionContext iScanConversionContext){
            mRenderscript = iRenderscript;
            mScriptCScanConversion = new ScriptC_scanConversion(mRenderscript);

            mRsIndexSampleLine = Allocation.createSized(mRenderscript, Element.I32(mRenderscript), iScanConversionContext.mIndexData.length);
            mRsIndexSampleLine.copyFrom(iScanConversionContext.mIndexData);
            mScriptCScanConversion.bind_index_samp_line(mRsIndexSampleLine);

            mRsImageIndex = Allocation.createSized(mRenderscript, Element.I32(mRenderscript), iScanConversionContext.mIndexImage.length);
            mRsImageIndex.copyFrom(iScanConversionContext.mIndexImage);
            mScriptCScanConversion.bind_image_index(mRsImageIndex);

            mRsIndexCounter = Allocation.createSized(mRenderscript, Element.I32(mRenderscript), iScanConversionContext.mIndexCounter.length);
            mRsIndexCounter.copyFrom(iScanConversionContext.mIndexCounter);
            mScriptCScanConversion.bind_index_counter(mRsIndexCounter);

            mRsWeight = Allocation.createSized(mRenderscript, Element.F64(mRenderscript), iScanConversionContext.mWeight.length);
            mRsWeight.copyFrom(iScanConversionContext.mWeight);
            mScriptCScanConversion.bind_weight_coef(mRsWeight);

            mRsOutputImage = Allocation.createSized(mRenderscript, Element.I32(mRenderscript), mImageOutput.length);
            mRsOutputImage.copyFrom(mImageOutput);
            mScriptCScanConversion.bind_output_image(mRsOutputImage);

            mScriptCScanConversion.set_num_samples_per_line(iScanConversionContext.mNbSamplesPerLines);

            mRsInputImage = Allocation.createSized(mRenderscript, Element.I32(mRenderscript), mImageInput.length);
        }
    }
}

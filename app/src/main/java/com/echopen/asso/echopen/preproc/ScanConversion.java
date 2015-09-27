package com.echopen.asso.echopen.preproc;

import com.echopen.asso.echopen.utils.Constants;

import java.util.ArrayList;

/**
 * Created by mehdibenchoufi on 16/09/15.
 */
public class ScanConversion {

    private int numPixels;

    private int numWeightPixels;

    private ArrayList<Integer> weight = new ArrayList<Integer>(numWeightPixels);

    private ArrayList<Integer> indexData = new ArrayList<Integer>(numPixels);

    private ArrayList<Integer> indexImg = new ArrayList<Integer>(numPixels);

    public int getNumPixels() {
        return numPixels;
    }

    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }

    public int getNumWeightPixels() {
        return numWeightPixels;
    }

    public void setNumWeightPixels(int numWeightPixels) {
        this.numWeightPixels = numWeightPixels;
    }

    public ArrayList<Integer> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<Integer> weight) {
        this.weight = weight;
    }


    public ArrayList<Integer> getIndexImg() {
        return indexImg;
    }

    public void setIndexImg(ArrayList<Integer> indexImg) {
        this.indexImg = indexImg;
    }

    public ArrayList<Integer> getIndexData() {
        return indexData;
    }

    public void setIndexData(ArrayList<Integer> indexData) {
        this.indexData = indexData;
    }

    public ScanConversion(int numPixels) {
        this.numPixels = numPixels;
    }

    /**
     * @param start_depth
     * @param image_size
     * @param start_of_data
     * @param delta_r
     * @param N_samples
     * @param theta_start
     * @param delta_theta
     * @param N_lines
     * @param scaling
     * @param Nz
     * @param Nx
     * @param weight_coef
     * @param index_samp_line
     * @param image_index
     * @return N_values Number of values to calculate in the image
     */
    private int make_tables(double start_depth,     /*  Depth for start of image in meters    */
                            double image_size,      /*  Size of image in meters               */

                            double start_of_data,   /*  Depth for start of data in meters     */
                            double delta_r,         /*  Sampling interval for data in meters  */
                            int N_samples,       /*  Number of data samples                */

                            double theta_start,     /*  Angle for first line in image         */
                            double delta_theta,     /*  Angle between individual lines        */
                            int N_lines,         /*  Number of acquired lines              */

                            double scaling,         /*  Scaling factor form envelope to image */
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

                    weight_coef[ij_index_coef] = (1 - samp_val) * (1 - line_val) * scaling;
                    weight_coef[ij_index_coef + 1] = samp_val * (1 - line_val) * scaling;
                    weight_coef[ij_index_coef + 2] = (1 - samp_val) * line_val * scaling;
                    weight_coef[ij_index_coef + 3] = samp_val * line_val * scaling;

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
        return N_values;

    }

    private void make_interpolation (char envelope_data[],   /*  The envelope detected and log-compressed data */
                             int            N_samples,        /*  Number of samples in one envelope line        */

                             int            index_samp_line[], /*  Index for the data sample number              */
                             int            image_index[],     /*  Index for the image matrix                    */
                             double          weight_coef[],     /*  The weight table                              */
                             int            N_values,         /*  Number of values to calculate in the image      */

                             char image[]            /*  The resulting image                           */
    ) {
        int           i;                 /*  Integer loop counter                */
        int           ij_index_coef;     /*  Index into coefficient array        */
        int env_index;      /*  Pointer to the envelope data        */
        int weight_index;   /*  Pointer to the weight coefficients  */

        ij_index_coef = 0;
        for (i=0; i<N_values; i++)
        {
            weight_index = ij_index_coef;
            env_index = index_samp_line[i];
            image[image_index[i]]= (char)(
                      weight_coef[weight_index] * envelope_data[env_index]
                    + weight_coef[weight_index + 1] * envelope_data[env_index + 1]
                    + weight_coef[weight_index + 2] * envelope_data[env_index + N_samples]
                    + weight_coef[weight_index + 3] * envelope_data[env_index + N_samples + 1]
                    + 0.5);
            ij_index_coef = ij_index_coef + 4;
        }
    }

    public void compute_tables() {
		double start_depth = Constants.PreProcParam.RADIAL_IMG_INIT; /*  Depth for start of image in meters    */
		double image_size = Constants.PreProcParam.IMAGE_SIZE;      /*  Size of image in meters               */

		double start_of_data = Constants.PreProcParam.STEP_RADIAL_INIT;   /*  Depth for start of data in meters     */
		double delta_r = Constants.PreProcParam.RADIAL_DATA_INIT * Math.floor(Constants.PreProcParam.NUM_SAMPLES / 1024; /*  Sampling interval for data in meters  */
		int N_samples  = (int) Math.floor(Constants.PreProcParam.NUM_SAMPLES / 1024);       /*  Number of data samples                */

		double theta_start = Constants.PreProcParam.NUM_LINES/ 2 * start_depth;     /*  Angle for first line in image         */
		double delta_theta = - Constants.PreProcParam.STEP_ANGLE_INIT; /*  Angle between individual lines        */
		int N_lines = Constants.PreProcParam.NUM_LINES;         /*  Number of acquired lines              */

		double scaling = Constants.PreProcParam.SCALE_FACTOR;         /*  Scaling factor form envelope to image */
		int Nz = Constants.PreProcParam.N_z;              /*  Size of image in pixels               */
		int Nx = Constants.PreProcParam.N_x;              /*  Size of image in pixels               */

		int Ncoef_max = 4;
		double weight_coef[Nz * Nx * Ncoef_max];    /*  The weight table                      */
		int index_samp_line[Nz * Nx];/*  Index for the data sample number      */
		int image_index[Nz * Nx];
		make_tables(start_depth, image_size, start_of_data, delta_r, N_samples, theta_start, delta_theta, N_lines, scaling, Nz, Nx, weight_coef, index_samp_line, image_index);
		// TODO convert arrays to fields.
    }

    public void compute_interpolation() {
        //TODO call make_interpolation with data from compute_tables and com.echopen.asso.echopen.utils.Constants
    }
}

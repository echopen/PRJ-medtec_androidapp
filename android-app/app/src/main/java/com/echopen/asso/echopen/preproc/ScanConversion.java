package com.echopen.asso.echopen.preproc;

import android.util.Log;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.model.Data.Data;
import com.echopen.asso.echopen.model.Data.ReadableData;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.UIParams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


/**
 * ScanConversion is the class that processes the scan conversion which allows one to
 * recreate a clinical image from a set of data sent by a probe.
 * The received image depends on the geometry of the probe. This process intends to recreate the ‘real’ image.
 * An ultrasound beamformer generates coherently summed image data in polar format while the standard TV raster display is rectangular.
 * Hence, polar to Cartesian scan conversion is necessary before display.
 *
 * Mainly, the scan conversion transform the incoming raw data in two steps
 *  - maps polar coordinates to cartesian coordinates
 *  - interpolates data : roughly speaking, steps between the spatial interval for data
 *  will not match with the step of two contiguous pixels. This create the need for interpolation.
 *
 *  In order to interpolate, we use the function make_tables() : this methods affects weights to the each
 *  4-uplets of pixels, that are as close to 1 as the the mismatch described above is weak
 *
 * @see more explantation : http://echopen.org/index.php?title=Challenge_scan_conversion, http://echopen.org/index.php?title=Scan_Conversion,
 */
public class ScanConversion {

    /* This class is a singleton Class*/
    public static ScanConversion singletonScanConversion = null;

    /* the number of pixels of the display app screen zone */
    private static int numPixels;

    /* the number of weight pixels, generally numWeightPixels = 4*numPixels */
    private static int numWeightPixels;

    /* the weight double array affects weight to each 4-uplets of pixels */
    private static double[] weight = new double[numWeightPixels];

    /* Index for the data sample number */
    private static int[] indexData = new int[numPixels];

    /* the specific index system of the image matrix */
    private static int[] indexImg = new int[numPixels];

    /* the ScanConversion class mainly processes data and outputs processed data stored in scannedArray*/
    private int[] scannedArray;

    /* for testing purpose, the data can be gathered from a csv file, this express the needs of an InputStreamReader */
    private InputStreamReader inputStreamReader;

    /* the incoming data comes in UDP format. It is stored in udpDataArray */
    private static int[][] udpDataArray;

    private static int[] image;
    private static int N_samples;
    private static int[] num;
    private static int[] envelope_data;
    private static byte[] envelope_data_bytes;

    private Random rnd = new Random();

    long lastTime;

    private static byte[] tcpDataArray;

    private short[] mEnvelopData;
    private Integer[] mTcpDataInt;

    {
        int rows = Constants.PreProcParam.NUM_IMG_DATA;
        int larger_rows = Constants.PreProcParam.opencv_RELATIVE_ANGLE;
        int cols = Constants.PreProcParam.NUM_SAMPLES;

        int Nz = Constants.PreProcParam.N_z;
        int Nx = Constants.PreProcParam.N_x;
    }

    /**
     * @param numPixels
     */
    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }

    /**
     * @return numWeightPixels
     */
    public int getNumWeightPixels() {
        return numWeightPixels;
    }

    /**
     * @param numWeightPixels
     */
    public void setNumWeightPixels(int numWeightPixels) {
        this.numWeightPixels = numWeightPixels;
    }

    /**
     * @return wieght
     */
    public double[] getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    /**
     * @return indexImg
     */
    public int[] getIndexImg() {
        return indexImg;
    }

    /**
     * @param indexImg
     */
    public void setIndexImg(int[] indexImg) {
        this.indexImg = indexImg;
    }

    /**
     * @return indexData
     */
    public int[] getIndexData() {
        return indexData;
    }

    /**
     * @param indexData
     */
    public void setIndexData(int[] indexData) {
        this.indexData = indexData;
    }

    /**
     * @return numPixels
     */
    public int getNumPixels() {
        return numPixels;
    }

    /**
     * @return scannedArray
     */
    public int[] getScannedArray() {
        return scannedArray;
    }

    /**
     * @param scannedArray
     */
    public void setScannedArray(int[] scannedArray) {
        this.scannedArray = scannedArray;
    }

    /**
     * @return udpDataArray
     */
    public int[][] getUdpDataArray() {
        return ScanConversion.udpDataArray;
    }

    /**
     * @param udpDataArray
     */
    public void setUdpDataArray(int[][] udpDataArray) {
        ScanConversion.udpDataArray = udpDataArray;
    }

    /**
     * ScanConversion constructor with InputStreamReader argument
     * for testing purpose, the data can be gathered from a csv file,
     * this express the needs of an InputStreamReader
     * @param inputStreamReader
     */
    public ScanConversion(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
    }

    public ScanConversion(final Data data) {
        setData(data);
    }

    /**
     * ScanConversion constructor with udpDataArray argument
     * @param udpDataArray
     */
    public ScanConversion(int[][] udpDataArray) {
        ScanConversion.udpDataArray = udpDataArray;
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * ScanConversion constructor with udpDataArray argument
     * @param tcpDataArray
     */
    public ScanConversion(byte[] tcpDataArray) {
        ScanConversion.tcpDataArray = tcpDataArray;
    }

    public ScanConversion(){

    }
    /**
     * Singleton getInstance method, with InputStreamReader argument
     * inputStreamReader holds the simulated data stored in a csv format,
     * in a file stored in the assets folder
     * @param inputStreamReader
     * @return
     */
    public static ScanConversion getInstance(InputStreamReader inputStreamReader) {
        if (singletonScanConversion == null) {
            singletonScanConversion = new ScanConversion(inputStreamReader);
            compute_tables();
        }
        return singletonScanConversion;
    }

    /**
     * Singleton getInstance method, with udpDataArray argument
     * udpDataArray holds the incoming data
     * @param udpDataArray
     * @return
     */
    public static ScanConversion getInstance(int[][] udpDataArray) {
        if (singletonScanConversion == null) {
            singletonScanConversion = new ScanConversion(udpDataArray);
            compute_tables();
        }
        ScanConversion.udpDataArray = udpDataArray;
        return singletonScanConversion;
    }

    public static ScanConversion getInstance(byte[] tcpDataArray) {
        if (singletonScanConversion == null) {
            singletonScanConversion = new ScanConversion(tcpDataArray);
        }
        ScanConversion.tcpDataArray = tcpDataArray;
        return singletonScanConversion;
    }

    public static ScanConversion getInstance(){
        if(singletonScanConversion == null){
            singletonScanConversion = new ScanConversion();
            return singletonScanConversion;
        }
        else{
            return singletonScanConversion;
        }
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
     * @param scaling
     * @param Nz
     * @param Nx
     * @param weight_coef
     * @param index_samp_line
     * @param image_index
     * @return N_values Number of values to calculate in the image
     */
    public static int make_tables(double start_depth,     /*  Depth for start of image in meters    */
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
        // as for a test purpose
        ScanConversion.numPixels = N_values;
        ScanConversion.indexData = index_samp_line;
        ScanConversion.indexImg = image_index;
        ScanConversion.weight =  weight_coef;

        return N_values;
    }

    /**
     * This method applies the weight coefficients (computed in make_tables()) and applies them
     * to the incoming envelope_data. This performs the two steps, namemy
     *  - maping polar coordinates to cartesian coordinates
     *  - interpolating data
     * @param envelope_data
     * @param N_samples
     * @param index_samp_line
     * @param image_index
     * @param weight_coef
     * @param N_values
     * @param image
     */
    private void make_interpolation (int envelope_data[],   /*  The envelope detected and log-compressed data */
                                     int            N_samples,        /*  Number of samples in one envelope line        */

                                     int            index_samp_line[], /*  Index for the data sample number              */
                                     int            image_index[],     /*  Index for the image matrix                    */
                                     double          weight_coef[],     /*  The weight table                              */
                                     int            N_values,         /*  Number of values to calculate in the image      */

                                     int image[]            /*  The resulting image                           */
    ) {
        if (image == null) {
            throw new IllegalArgumentException("image must not be null");
        }
        if (image_index == null) {
            throw new IllegalArgumentException("image_index must not be null");
        }
        if (envelope_data == null) {
            throw new IllegalArgumentException("envelope_data must not be null");
        }
        if (weight_coef == null) {
            throw new IllegalArgumentException("weight_coef must not be null");
        }
        if (index_samp_line == null) {
            throw new IllegalArgumentException("index_samp_line must not be null");
        }
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

    /**
     * This method is preparing the make_tables() method call. All the constants are set into variables passed as
     * arguments to the make_tables()
     * These arguments are essentially, the depth for start of image in meters, the size of image in meters,
     * the sampling interval for data in meters, the number of data samples, the angle between individual lines,
     * the angle for first line in image, the number of acquired lines, the scaling factor form envelope to image,
     * the size of image in pixels.
     *
     * All theses constants are set from the Constants class.
     */
    public static void compute_tables() {
        double start_depth = Constants.PreProcParam.RADIAL_IMG_INIT; /*  Depth for start of image in meters    */
        double image_size = Constants.PreProcParam.IMAGE_SIZE;      /*  Size of image in meters               */

        double start_of_data = Constants.PreProcParam.STEP_RADIAL_INIT;   /*  Depth for start of data in meters     */
        double delta_r = Constants.PreProcParam.RADIAL_DATA_INIT; /*  Sampling interval for data in meters  */
        int N_samples  = (int) Math.floor(Constants.PreProcParam.NUM_SAMPLES);       /*  Number of data samples                */

        double delta_theta = Constants.PreProcParam.STEP_ANGLE_INIT; /*  Angle between individual lines        */
        double theta_start = Constants.PreProcParam.NUM_LINES/ 2 * delta_theta;     /*  Angle for first line in image         */


        int N_lines = Constants.PreProcParam.NUM_LINES;         /*  Number of acquired lines              */

        double scaling = Constants.PreProcParam.SCALE_FACTOR;         /*  Scaling factor form envelope to image */
        int Nz = Constants.PreProcParam.N_z;              /*  Size of image in pixels               */
        int Nx = Constants.PreProcParam.N_x;              /*  Size of image in pixels               */

        int Ncoef_max = 4;
        double weight_coef[] = new double[Nz * Nx * Ncoef_max];    //*  The weight table                      *//*
        int index_samp_line[] = new int[Nz * Nx];  //Index for the data sample number
        int image_index[] = new int[Nz * Nx];

        make_tables(start_depth, image_size, start_of_data, delta_r, N_samples, theta_start, -delta_theta, N_lines, scaling, Nz, Nx, weight_coef, index_samp_line, image_index);
        // TODO convert arrays to fields.*/

        image = new int[Nz * Nx];
        num = new int[Nz * Nx];
    }

    public int[] getDataFromInterpolation(){
       /* try {
            if(MainActivity.UDP_ACQUISITION || MainActivity.TCP_ACQUISITION)
                //TODO:replace with scan conversion filter
                return null;
            else
                return compute_interpolation();
        } catch (IOException e) {
        }*/
        return null;

//        try {
//            return compute_interpolation();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public void setData(final Data value) {
        if (value == null) {
            throw new IllegalArgumentException("Data value must not be null");
        }
        assert(value != null);
        envelope_data = value.getEnvelopeData();
        if (envelope_data == null) {
            throw new IllegalArgumentException("The Envelope Data must not be null");
        }
    }

    public void randomize() {
        int x = rnd.nextInt(envelope_data.length);
        envelope_data[x] = rnd.nextInt(255);
    }

    public void setUdpData() {
        final ReadableData echoData = new ReadableData(ScanConversion.udpDataArray, int.class);
        setData(echoData);
    }

    public void setTcpData() {
        envelope_data_bytes = ScanConversion.tcpDataArray;
    }

    public void setTcpDataInt(Integer[] iTcpData)
    {
        mTcpDataInt = iTcpData;
    }
    /**
     * Fetches the UDP data stored in udpDataArray, and then passes it to make_interpolation()
     * in order to scan converts it.
     * This produces an image array of scan converted data, which corresponds precisely to the pixel image
     * @return int[], array of pixels corresponding to the  scan converted image
     * @throws IOException
     */
    private int[] compute_interpolation() throws IOException {
        assert(envelope_data != null);
        if (image == null || num == null) {
            compute_tables();
        }
        assert(num != null);
        assert(image != null);

        int Nz = Constants.PreProcParam.N_z;
        int Nx = Constants.PreProcParam.N_x;

        make_interpolation(envelope_data, N_samples, ScanConversion.indexData, ScanConversion.indexImg, ScanConversion.weight, ScanConversion.numPixels, image);

        for (int i = 0; i < Nz; i++) {
            for (int j = 0; j < Nx ; j++) {
                num[j*Nz + i] = image[j + Nx*i];
            }
        }
        return num;
    }
}

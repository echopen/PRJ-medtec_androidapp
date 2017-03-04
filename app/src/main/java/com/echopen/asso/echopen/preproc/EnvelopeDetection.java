package com.echopen.asso.echopen.preproc;

import com.echopen.asso.echopen.envelope_detection.FFT;

/**
 * Created by mehdibenchoufi on 21/02/17.
 */

public class EnvelopeDetection {

    private double[] raw_data;

    private double[] refined_data;

    private static double[] null_data = new double[8];

    public double[] getRefined_data() {
        return refined_data;
    }


    public EnvelopeDetection() {
    }

    public EnvelopeDetection(double[] raw_data) {
        this.raw_data = raw_data;
    }

    public double[] FFTransform() {

        FFT fft = new FFT(3);
        fft.doFFT(raw_data,null_data,true);
        refined_data = fft.getReal_part_data();
        double[] imaginary_data = fft.getImaginary_part_data();
        return refined_data;
    }

    public double[] InverseFFTransform() {
        return refined_data;
    }
}

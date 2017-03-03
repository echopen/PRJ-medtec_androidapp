package com.echopen.asso.echopen.preproc;

/**
 * Created by mehdibenchoufi on 22/02/17.
 */

import com.echopen.asso.echopen.preproc.envelope_detection.jtransforms.fft.DoubleFFT_1D1TAlgorithm;

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

    public void FFTransform() {
        new DoubleFFT_1D1TAlgorithm(0,0).perform(raw_data);
    }

    public double[] InverseFFTransform() {
        return refined_data;
    }
}

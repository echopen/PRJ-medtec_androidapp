package com.echopen.asso.echopen.envelope_detection.jtransforms.fft;

import com.echopen.asso.echopen.envelope_detection.jtransforms.fft.*;
import com.echopen.asso.echopen.envelope_detection.jtransforms.fftw.*;

public class DoubleFFT_1D1TAlgorithm extends com.echopen.asso.echopen.envelope_detection.DspAlgorithm {
	
	public DoubleFFT_1D1TAlgorithm(int sRate, int bSize) {
		super(sRate, bSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void perform(double[] buffer) {
		// TODO Auto-generated method stub
		com.echopen.asso.echopen.envelope_detection.jtransforms.fft.DoubleFFT_1D fft = new DoubleFFT_1D(buffer.length/2);
		ConcurrencyUtils.setNumberOfThreads(1);
		fft.complexForward(buffer);
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "DoubleFFT 1D - 2 threads";
	}

}

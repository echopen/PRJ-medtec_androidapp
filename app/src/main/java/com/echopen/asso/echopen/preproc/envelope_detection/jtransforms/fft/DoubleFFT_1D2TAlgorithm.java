package com.echopen.asso.echopen.preproc.envelope_detection.jtransforms.fft;

import com.echopen.asso.echopen.preproc.envelope_detection.jtransforms.fftw.ConcurrencyUtils;
import com.echopen.asso.echopen.preproc.envelope_detection.jtransforms.fftw.DspAlgorithm;

public class DoubleFFT_1D2TAlgorithm extends DspAlgorithm {
	
	public DoubleFFT_1D2TAlgorithm(int sRate, int bSize) {
		super(sRate, bSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void perform(double[] buffer) {
		// TODO Auto-generated method stub
		DoubleFFT_1D fft = new DoubleFFT_1D(buffer.length/2);
		ConcurrencyUtils.setNumberOfThreads(2);
		fft.complexForward(buffer);
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "DoubleFFT 1D - 2 threads";
	}

}

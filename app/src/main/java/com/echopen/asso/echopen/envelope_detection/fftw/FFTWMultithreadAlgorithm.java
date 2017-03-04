package com.echopen.asso.echopen.envelope_detection.fftw;

public class FFTWMultithreadAlgorithm extends com.echopen.asso.echopen.envelope_detection.DspAlgorithm {

	public FFTWMultithreadAlgorithm(int sRate, int bSize) {
		super(sRate, bSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void perform(double[] buffer) {
		// TODO Auto-generated method stub
		FFTW.setMultithread(2);
		FFTW.execute(buffer);		
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "FFTW - multithread";
	}

}

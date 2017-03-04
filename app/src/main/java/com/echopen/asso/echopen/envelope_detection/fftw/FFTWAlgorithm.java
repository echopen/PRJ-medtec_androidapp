package com.echopen.asso.echopen.envelope_detection.fftw;

public class FFTWAlgorithm extends com.echopen.asso.echopen.envelope_detection.DspAlgorithm {
	
	public FFTWAlgorithm(int sRate, int bSize) {
		super(sRate, bSize);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void perform(double[] buffer) {
		// TODO Auto-generated method stub
		FFTW.setMonothread();
		FFTW.execute(buffer);
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "FFTW - monothread";
	}

}

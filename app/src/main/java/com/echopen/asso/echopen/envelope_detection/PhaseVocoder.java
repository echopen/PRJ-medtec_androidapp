package com.echopen.asso.echopen.envelope_detection;


/**
 * ATTENTION: This class is currently not used. This is on of the reasons
 * why is is not properly documented.
 * 
 * @author andrejb
 *
 */
public class PhaseVocoder extends DspAlgorithm {

	private double mag[];
	private double last_mag[];
	private double phase[];
	private double last_phase[];
	private double ind[];
	private double deltak[];
	private FFT fft;
	
	private static final double TWOPI = 2.0 * Math.PI;
	
	
	public PhaseVocoder(int sRate, int bSize) {
		super(sRate, bSize);
		mag = new double[getBlockSize()];
		last_mag = new double[getBlockSize()];
		phase = new double[getBlockSize()];
		last_phase = new double[getBlockSize()];
		ind = new double[getBlockSize()];
		for (int i = 0; i < getBlockSize(); i++) {
			last_mag[i] = 0;
			last_phase[i] = 0;
			ind[i] = 0;
		}
		fft = new FFT((int) (Math.log10(getBlockSize()) / Math.log10(2)));
	}

	@Override
	public void perform(double[] buffer) {
		// Convert input to doubles.
		double real[] = new double[buffer.length];
		double imag[] = new double[buffer.length];
		for (int i = 0; i < real.length; i++) {
			imag[i] = 0;
		}
		// Do PV analysis
		Analysis(real, imag);
		//double out[] = Synthesis(real, imag);
		//System.arraycopy(buffer, 0, out, 0, getBlockSize());
		Synthesis(real, imag);
	}
	
	private void Analysis(double real[], double imag[]) {
		// Perform FFT on input
		FFT(real, imag, false);
		for (int k = 0; k < real.length; k++) {
			mag[k] = 2 * Math.sqrt(Math.pow(real[k],2)+Math.pow(imag[k],2));
			double ph = Math.atan2(imag[k], real[k]);
			double deltaphi = modulo2pi(ph - last_phase[k]);
			phase[k] = (deltaphi / TWOPI + k) * getSampleRate() / getBlockSize();
			last_phase[k] = ph;
		}
	}
	
	private double[] Synthesis(double real[], double imag[]) {
		double out[] = new double[real.length];
		for (int k = 0; k < getBlockSize() / 2; k++) {
			deltak[k] = getParameter1()*phase[k];
		}
		for (int t = 0; t < real.length; t++) {
			double lambda = t / (int) (getBlockSize() + 0.5);
			out[t] = 0;
			for (int k = 0; k < getBlockSize() / 2; k++) {
				double amplitude = (1.0 - lambda)*last_mag[k] + lambda*mag[k];
				out[t] += amplitude * Math.sin(ind[k]+t*getParameter1()*phase[k]*TWOPI/getSampleRate());
			}
		}
		for (int k = 0; k < getBlockSize() / 2; k++) {
			ind[k] = modulo2pi(ind[k]+getBlockSize()*phase[k]*TWOPI/getSampleRate());
			last_mag[k] = mag[k];
		}
		return out;
	}

	private void FFT(double real[], double imag[], boolean flagInvert) {
		fft.doFFT(real, imag, flagInvert);
	}
	
	private double modulo2pi(double x) {
		if (x >= 0) {
			x -= Math.floor(x / TWOPI) * TWOPI;
			if (x > Math.PI)
				x -= TWOPI;
		} else {
			x += Math.floor(-x / TWOPI) * TWOPI;
			if (x < - Math.PI)
				x += TWOPI;
		}
		return x;
	}
	
	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Phase Vocoder";
	}

}

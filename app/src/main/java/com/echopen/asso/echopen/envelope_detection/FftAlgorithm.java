package com.echopen.asso.echopen.envelope_detection;



/**
 * A simple algorithm that calculates a one way FFT.
 * @author andrejb
 *
 */
public class FftAlgorithm extends DspAlgorithm {

	private FFT fft = null;
	
	/*************************************************************************
	 * Constructor.
	 ***********************************************************************/
	
	public FftAlgorithm(int sRate, int bSize) {
		super(sRate, bSize);
	}
	
	/*************************************************************************
	 * Perform method.
	 ***********************************************************************/

	/**
	 * Perform a one-way FFT.
	 */
	@Override
	public void perform(double[] real) {
		double imag[] = new double[real.length];
		java.util.Arrays.fill(imag, 0);
		fft.doFFT(real, imag, false);
		//fft.doFFT(real, imag, true);
	}
	
	/**
	 * Reconfigure the FFT when block size changes.
	 */
	public void setBlockSize(int bSize) {
		super.setBlockSize(bSize);
		if (fft == null)
			fft = new FFT((int) (Math.log10(bSize) / Math.log10(2)));
		else
			fft.setBits((int) (Math.log10(getBlockSize()) / Math.log10(2)));
	}
	
	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "FFT";
	}
}

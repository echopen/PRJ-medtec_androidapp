package com.echopen.asso.echopen.envelope_detection;


/**
 * A simple IIR reverberation filter.
 * 
 * @author andrejb
 *
 */
public class Reverb extends DspAlgorithm {
	
	private double oldOutput[];
	private double oldInput[];

	public Reverb(int sRate, int bSize) {
		super(sRate, bSize);
		oldOutput = new double[bSize];
		oldInput = new double[bSize];
		java.util.Arrays.fill(oldOutput, 0);
	}
	
	public void perform(double[] buffer) {
		// holds old input values
		double[] tmpOldInput = new double[this.getBlockSize()];
		System.arraycopy(buffer, 0, tmpOldInput, 0, this.getBlockSize());
		// number of delay samples
		int m = (int) (this.getParameter1() * this.getBlockSize());
		if (m < 1)
			m=1;
		// feedback gain
		double g = 0.7;
		//double g = this.getParameter1();
		
		// perform the reverb
		for (int i = 0; i < m; i++)
			buffer[i] = -g * buffer[i] + oldInput[this.getBlockSize()-m+i] + g * oldOutput[this.getBlockSize()-m+i];
		for (int i = m; i < this.getBlockSize(); i++)
			buffer[i] = -g * buffer[i] + tmpOldInput[i-m] + g * buffer[i-m];
		// finish
		oldInput = tmpOldInput;
		System.arraycopy(buffer, 0, oldOutput, 0, this.getBlockSize());

	}
	
	/**
	 * Take care of reinitialize arrays when block size is changed.
	 */
	public void setBlockSize(int bSize) {
		super.setBlockSize(bSize);
		oldOutput = new double[bSize];
		oldInput = new double[bSize];
		java.util.Arrays.fill(oldOutput, 0);
	}
	
	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "IIR Filter Reverb";
	}
}

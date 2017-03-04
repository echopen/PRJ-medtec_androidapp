package com.echopen.asso.echopen.envelope_detection;


/**
 * Additive Synthesis using Java's Math.sin() method. It uses a number of oscillators 
 * equal to the stressParameter. 
 *  
 * @author andrejb
 *
 */
public class AdditiveSynthesisSine extends StressAlgorithm {

	protected static final double TWOPI = 2.0 * Math.PI;
	
	private int lastInd;  // Used to preserve phase.
	
	private float coefficient;
	
	public AdditiveSynthesisSine(int sRate, int bSize, int stressParam) {
		super(sRate, bSize);
		setStressParameter(stressParam);
		lastInd = 0;
	}

	/**
	 * The perform method is 
	 */
	@Override
	public void perform(double[] buffer) {
		for (int n = 0; n < buffer.length; n++) {
			buffer[n] = 0;
			for (int i = 1; i <= stressParameter; i++)
				buffer[n] += coefficient*Math.sin(TWOPI*110*(lastInd+n)*i/sampleRate);
		}
		lastInd += buffer.length;  // preserve phase
	}
	
	/**
	 * When changing the parameter using the GUI, also update the number of
	 * oscillators used in calculation.
	 */
	public void setParams(double param1) {
		super.setParams(param1);
		setStressParameter((int) (10 * param1));
	}
	
	/**
	 * Set the block size and update the coefficient.
	 * 
	 * @param bSize
	 */
	public void setBlockSize(int bSize) {
		super.setBlockSize(bSize);
		if (stressParameter != 0)
			coefficient = 1/stressParameter;
		else
			coefficient = 1;
	}
	
	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Additive Synthesis (sine)";
	}
}

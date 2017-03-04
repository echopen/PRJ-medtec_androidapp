package com.echopen.asso.echopen.envelope_detection;


/**
 * A stress algorithm is an algorithm that may change it's computational cost
 * so during a test it this cost can either be incremented or decremented to
 * determine if a given algorithm is feasible in a specific device. 
 * 
 * @author andrejb
 *
 */
public abstract class StressAlgorithm extends DspAlgorithm {

	/**
	 * This parameter should be used by stress algorithms to control the size
	 * or computational intensity of their work.
	 */
	protected int stressParameter = 1;
	
	/**
	 * The constructor just saves the sample rate and block size.
	 * 
	 * @param sRate
	 * @param bSize
	 */
	public StressAlgorithm(int sRate, int bSize) {
		super(sRate, bSize);
	}
	
	/**
	 * Set the stress parameter.
	 * 
	 * @param fSize
	 */
	public void setStressParameter(int stressParam) {
		stressParameter = stressParam;
		if (stressParameter == 0)
			stressParameter = 1;
	}
	
}

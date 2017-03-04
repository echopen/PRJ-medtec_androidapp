package com.echopen.asso.echopen.envelope_detection;


/**
 * A simple loopback algorithm, that actually does
 * nothing. It is meant to be used as a reference of the minimal time the
 * DSP infrastructure takes. 
 * 
 * @author andrejb
 *
 */
public class Loopback extends DspAlgorithm {

	public Loopback(int sRate, int bSize) {
		super(sRate, bSize);
		// TODO Auto-generated constructor stub
	}

	public void perform(double[] buffer) {

	}
	
	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Loopback";
	}
	
}

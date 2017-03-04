package com.echopen.asso.echopen.envelope_detection;

import java.util.Random;

/**
 * A random FIR filter that may vary it's length. The idea is just to be able
 * to know if arbitrary filters of a given size are feasible in a specific
 * device. 
 * 
 * @author andrejb
 *
 */
public class Convolution extends StressAlgorithm {

	private double[] coef = new double[10];

	/**
	 * Configure the convolution algorithm.
	 * @param sRate
	 * @param bSize
	 * @param convSize
	 */
	public Convolution(int sRate, int bSize, int convSize) {
		super(sRate, bSize);
		calcCoef();
		setStressParameter(convSize);
	}

	/*************************************************************************
	 * Algorithm setup
	 ************************************************************************/

	/**
	 * This calculates random coefficients to be used during a filter's
	 * comptuation.
	 */
	private void calcCoef() {
		Random r = new Random();
		for (int k = 0; k < coef.length; k++)
			coef[k]= r.nextDouble();
	}

	/*************************************************************************
	 * Perform
	 ************************************************************************/
	@Override
	public void perform(double[] buffer) {
		for (int n = 0; n < buffer.length; n++) {
			buffer[n] = wmean(buffer, n);
		}
	}

	private double wmean(double buffer[], int n) {
		double sum = 0;
		for (int k = 0; k < stressParameter; k++)
			sum += coef[k % coef.length]*buffer[(n+k)%buffer.length];
		return sum / n;
	}

	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Convolution";
	}
}

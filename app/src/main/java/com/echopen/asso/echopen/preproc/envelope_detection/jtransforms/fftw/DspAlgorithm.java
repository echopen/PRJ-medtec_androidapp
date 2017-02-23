package com.echopen.asso.echopen.preproc.envelope_detection.jtransforms.fftw;

/**
 * An abstract representation of a DSP algorithm. It stores the current DSP
 * block size and the sample rate of the signal it is working over.
 * 
 * All DSP algorithms should have a perform() method that works over a buffer
 * containing the signal samples and actually modifies/analyses/synthesises
 * the signal.
 * 
 * To add a new DSP algorithm, do the following:
 * 
 *   1. Create a class extending DspAlgorithm.
 *
 *   2. Write the perform method for the new algorithm inside the new class,
 *      also adding whatever is needed for the algorithm to work.
 *
 *   3. Instantiate the new algorithm and add it to the `algorithms` vector in
 *       DspThread's constructor.  
 *
 *   4. If you want the new algorithm to be available to the user on the GUI,
 *      add the new algorithm name to the `algorithm_array` array in
 *      `res/values/strings.xml` (that array populates the pulldown menu on
 *      the GUI).
 *
 *   5. Include the new algorithm in the tests by modifying AllTestsAlgorithm.
 *
 * 
 * @author andrejb
 *
 */
public abstract class DspAlgorithm {

	protected int blockSize;
	protected int sampleRate;
	
	protected double parameter1 = 1;
	
	/*************************************************************************
	 * Constructor
	 ************************************************************************/
	
	/**
	 * The constructor just saves the sample rate and block size of the
	 * current algorithm.
	 * 
	 * @param sRate
	 * @param bSize
	 */
	public DspAlgorithm(int sRate, int bSize) {
		sampleRate = sRate;
		setBlockSize(bSize);
	}

	/*************************************************************************
	 * Perform method
	 ************************************************************************/
	
	/**
	 * This method should be implemented by all DSP algorithms. It is the
	 * actual perform function that works over a buffer to modify the audio
	 * signal.
	 * 
	 * @param buffer
	 */
	abstract public void perform(double[] buffer);
	
	/*************************************************************************
	 * Setters/getters
	 ************************************************************************/
	
	/**
	 * Set the block size.
	 * 
	 * @param bSize
	 */
	public void setBlockSize(int bSize) {
		blockSize = bSize;
	}
	
	/**
	 * @return The current block size.
	 */
	public int getBlockSize() {
		return blockSize;
	}
	
	/**
	 * @return The current sample rate.
	 */
	public int getSampleRate() {
		return sampleRate;
	}
	
	/**
	 * Set a parameter to control the algorithm.
	 * @param param1
	 */
	public void setParams(double param1) {
		parameter1 = param1;
	}
	
	/**
	 * @return The parameter that controls the algorithm.
	 */
	public double getParameter1() {
		return parameter1;
	}
	
	/**
	 * @return The name of the algorithm.
	 */
	abstract public String getAlgorithmName();
	
}

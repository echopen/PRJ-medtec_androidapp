package com.echopen.asso.echopen.envelope_detection;

/**
 * An additive synthesis using table lookup with a truncated index.
 * 
 * @author andrejb
 *
 */
public class AdditiveSynthesisLookupTableTruncated extends
		AdditiveSynthesisLookupTable {

	/**
	 * Constructor.
	 * 
	 * @param sRate
	 * @param bSize
	 * @param stressParam
	 */
	public AdditiveSynthesisLookupTableTruncated(int sRate, int bSize,
			int stressParam) {
		super(sRate, bSize, stressParam);
	}
	
	/**
	 * Truncated table lookup interpolation.
	 */
	protected float lookup(float i)
	{
		int i0;
		i0 = ((int)i) % sine.length;
		if (i0 < 0)
			i0 += sine.length;
		return sine[i0];
	}
	
	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Additive Synthesis (truncated)";
	}
}

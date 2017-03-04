package com.echopen.asso.echopen.envelope_detection;

/**
 * Additives synthesis using table lookup with linear interpolation.
 * 
 * @author andrejb
 *
 */
public class AdditiveSynthesisLookupTableLinear extends
		AdditiveSynthesisLookupTable {

	/**
	 * Constructor.
	 * 
	 * @param sRate
	 * @param bSize
	 * @param stressParam
	 */
	public AdditiveSynthesisLookupTableLinear(int sRate, int bSize,
			int stressParam) {
		super(sRate, bSize, stressParam);
	}

	/**
	 * Linear interpolation.
	 */
	protected float lookup(float i)
	{
		float y = i - (int) i;
		int i0,i1;
		i0 = ((int)i) % sine.length;
		if (i0 < 0)
			i0 += sine.length;
		i1 = (i0+1) % sine.length;
		if (i1 < 0)
			i1 += sine.length;
		return (sine[i1] - sine[i0])*y + sine[i0];
	}

	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Additive Synthesis (linear)";
	}
}

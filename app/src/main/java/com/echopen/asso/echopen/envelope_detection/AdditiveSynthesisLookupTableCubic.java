package com.echopen.asso.echopen.envelope_detection;

/**
 * Additive synthesis using table lookup with cubic interpolation.
 * 
 * @author andrejb
 *
 */
public class AdditiveSynthesisLookupTableCubic extends
AdditiveSynthesisLookupTable {


	public AdditiveSynthesisLookupTableCubic(int sRate, int bSize,
			int stressParam) {
		super(sRate, bSize, stressParam);
	}

	protected float lookup(float i)
	{
		float y = i - (int) i;
		int i0,i1,i2,i3;
		i1 = ((int)i) % sine.length;
		i0 = (i1-1) % sine.length;
		i2 = (i1+1) % sine.length;
		i3 = (i1+2) % sine.length;
		if (i0 < 0)
			i0 += sine.length;
		if (i1 < 0)
			i1 += sine.length;
		if (i2 < 0)
			i2 += sine.length;
		if (i3 < 0)
			i3 += sine.length;
		return -y*(y-1)*(y-2)*sine[i0]/6+(y+1)*(y-1)*(y-2)*sine[i1]/2-(y+1)*y*(y-2)*sine[i2]/2+(y+1)*y*(y-1)*sine[i3]/6;
	}

	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Additive Synthesis (cubic)";
	}
}

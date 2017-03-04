package com.echopen.asso.echopen.envelope_detection;


/**
 * ATTENTION: This class is currently not used. This is on of the reasons
 * why is is not properly documented.
 * 
 * @author andrejb
 *
 */
public class PitchShifting extends DspAlgorithm {

	public PitchShifting(int sRate, int bSize) {
		super(sRate, bSize);
	}

	/**
	 * Shifts the pitch of the incoming signal.
	 */
	public void perform(double[] buffer) {
		// Pitch Shifting parameters
		int N = this.getBlockSize();						// block length
		int Sa = N / 2;										// analysis hop size
		double alpha = this.getParameter1() * 1.75 + 0.25;	// pitch scaling factor
		int L = (int) (256 * alpha / 2);					// overlap interval
		int M = (int) Math.ceil(this.getBlockSize() / Sa);
		int Ss = (int) Math.round(Sa*alpha);
		
		buffer[M*Sa+N] = 0;
		
		//===================================================================
		// TimeScaleSOLA loop.
		//===================================================================
		// Time Stretching using alpha2 = 1/alpha
		for (int ni = 0; ni < M-1; ni++) {
			// grain
			int grainStart = ni*Sa+1;
			int grainEnd = N+ni*Sa;
			int grainLength = grainEnd - grainStart + 1;
			short[] grain = new short[grainLength];
			System.arraycopy(buffer, grainStart, grain, 0, grainLength);
			
			// overlap
			short[] overlap = new short[L];
			
			
			short[] XCORRsegment = xcorr(grain, overlap);
			int km = getIndexOfMax(XCORRsegment);
			
			// fadeout
			double fadeStep = 1.0 / (overlap.length-(ni*Ss-(L-1)+km-1));
			int fadeLength = (int) (1.0 / fadeStep) + 1;
			double[] fadeout = new double[fadeLength];
			for (int i =0; i < fadeout.length; i++)
				fadeout[i] = 1 - i*fadeStep;
			
			// fadein
			double[] fadein = new double[fadeLength];
			for (int i =0; i < fadein.length; i++)
				fadein[i] = 0 + i*fadeStep;
			
			// tail
			//short[] Tail = dotMultiply(overlap, fadeout);
			//short[] Begin = dotMultiply(grain, fadein);
			
		}
	}
	
	/**
	 * Calculates the correlation between two vectors.
	 * @param x1
	 * @param x2
	 * @return
	 */
	private short[] xcorr(short[] x1, short[] x2)
	{
		int L = x1.length;
		short[] r = new short[L];
		for (int k = 0; k < L; k++) {
			r[k] = 0;
			for (int n=0; n < L-k; n++)
				r[k] += x1[n]*x2[n+k];
			r[k] /= L; 
		}
		return r;
	}
	
	/**
	 * Returns the (first) index of the maximum value of an array.
	 * @param a
	 * @return
	 */
	private int getIndexOfMax(short[] a) {
		int k = 0;
		for (int i = 0; i < a.length; i++)
			if (a[i] > a[k])
				k = i;
		return k;
	}
	
	/**
	 * Dot-Multiply: each position of the resulting array is the multiplication of the corresponding position of the input arrays.
	 * @param x1
	 * @param x2
	 * @return
	 */
	//private short[] dotMultiply(short[] x1, double[] x2) {
	//	short[] r = new short[x1.length];
	//	for (int i = 0; i < r.length; i++)
	//		r[i] = (short) (x1[i] * x2[i]);
	//	return r;
	//}

	/**
	 * @return The name of the algorithm.
	 */
	public String getAlgorithmName()
	{
		return "Pitch Shifting";
	}
}

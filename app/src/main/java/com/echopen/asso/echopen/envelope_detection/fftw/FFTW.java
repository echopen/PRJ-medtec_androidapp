package com.echopen.asso.echopen.envelope_detection.fftw;

public class FFTW {

    static {
        System.loadLibrary("fftw_jni"); 
    }

	private static native double[] executeJNI(double in[]);
	private static native void initThreadsJNI(int num_of_threads);
	private static native boolean areThreadsEnabled();
	private static native void removeThreadsJNI();
	
	public static void setMultithread(int num_of_threads) {
		if (!FFTW.areThreadsEnabled()) {
			FFTW.initThreadsJNI(num_of_threads);
		}
	}
	
	public static void setMonothread() {
		if (FFTW.areThreadsEnabled()) {
			FFTW.removeThreadsJNI();
		}
	}
	
	public static double[] execute(double in[]) {
		return FFTW.executeJNI(in);  
	}
	
}


public class ProbeServer {

	private final String BATTERY_THREAD = "BATTERY THREAD";
	private final String IMAGE_THREAD = "IMAGE THREAD";

	private Thread mImageStreamingThread;
	private Thread mBatteryThread;
	
	public void start() {
		mImageStreamingThread = new Thread(new ImageStreamingRunnable(), IMAGE_THREAD);
		mImageStreamingThread.start();
		//mBatteryThread = new Thread(lBatteryThreadRunnable, BATTERY_THREAD);
	}
	
	public void stop() {
		mImageStreamingThread.stop();
	}
}

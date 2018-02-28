
public class ProbeImageConfiguration {

	private short mR0;
	private short mRf;
	private byte mDecimation;
	private short mNlines;
	private byte mSector;
	private byte mMode_RP;
	
	public ProbeImageConfiguration(short iR0, short iRf, byte iDecimation, short iNlines, byte iSector, byte iMode_RP) {
		mR0 = iR0;
		mRf = iRf;
		mDecimation = iDecimation;
		mNlines = iNlines;
		mSector = iSector;
		mMode_RP = iMode_RP;
		System.out.println("Image Configuration: " + mR0 + " " + mRf + " " + mDecimation + " " + mNlines + " " + mSector + " " + mMode_RP);
	}
	
	public byte[] getBytesArray() {
		return new byte[]{(byte)mR0, (byte)mRf, mDecimation, (byte)mNlines, mSector, mMode_RP};
	}

	public short getNLines() {
		return mNlines;
	}

}

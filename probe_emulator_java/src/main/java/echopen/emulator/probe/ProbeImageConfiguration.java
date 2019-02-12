package echopen.emulator.probe;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ProbeImageConfiguration {

	private float mR0;
	private float mRf;
	private float mDecimation;
	private short mNlines;
	private float mSector;
	private byte mMode_RP;
	private String mProbeCinematicName;
	private float mEchoDelay;

	public ProbeImageConfiguration(float iR0, float iRf, float iDecimation, short iNlines, float iSector, byte iMode_RP, String iProbeCinematicName, float iEchoDelay) {
		mR0 = iR0;
		mRf = iRf;
		mDecimation = iDecimation;
		mNlines = iNlines;
		mSector = iSector;
		mMode_RP = iMode_RP;
		mProbeCinematicName = iProbeCinematicName;
		mEchoDelay = iEchoDelay;
		System.out.println(this);
	}

	public byte[] getBytesArray() {
		ByteArrayOutputStream lByteArrayOutput = new ByteArrayOutputStream();
		DataOutputStream lDataOutput = new DataOutputStream(lByteArrayOutput);
		byte[] oByteArray = null;
		try{
			lDataOutput.writeFloat(mR0);
			lDataOutput.writeFloat(mRf);
			lDataOutput.writeFloat(mDecimation);
			lDataOutput.writeShort(mNlines);
			lDataOutput.writeFloat(mSector);
			lDataOutput.writeByte(mMode_RP);
			int lProbeNameLength = mProbeCinematicName.getBytes().length;
			lDataOutput.writeInt(lProbeNameLength);
			lDataOutput.write(mProbeCinematicName.getBytes());
			lDataOutput.writeFloat(mEchoDelay);

			oByteArray = lByteArrayOutput.toByteArray();
			lByteArrayOutput.close();
			lDataOutput.close();
		}
		catch(Exception IOException){}

		return oByteArray;
	}

	public short getNLines() {
		return mNlines;
	}

	public String toString() {
			return "Image Configuration: " + mR0 + " " + mRf + " " + mDecimation + " " + mNlines + " " + mSector + " " + mMode_RP + " " + mProbeCinematicName.getBytes().length + " " + mProbeCinematicName + " " + mEchoDelay;
	}

}

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UltrasoundImage {
	private short[] mPixelValues;
	private short mNbPixelsPerLine;
	private short mNbLinesPerImage;
	
	public UltrasoundImage(short[] iPixelValues,short iNbPixelsPerLine,short iNbLinesPerImage) {
		mPixelValues = iPixelValues;
		mNbPixelsPerLine = iNbPixelsPerLine;
		mNbLinesPerImage = iNbLinesPerImage;
	}
	
	public short getNbLinesPerImage() {
		return mNbLinesPerImage;
	}
	
	public byte[] getNthLineAsByteArray(short iLineIndex) {
		ByteBuffer lByteBuffer = ByteBuffer.allocate(2*mNbPixelsPerLine + 2);
		lByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		lByteBuffer.putShort((short) (iLineIndex+1));
		for(int i = iLineIndex * mNbPixelsPerLine; i < (iLineIndex + 1) * mNbPixelsPerLine; i++) {
			lByteBuffer.putShort(mPixelValues[i]);
		}
		return lByteBuffer.array();
	}
	
	
}

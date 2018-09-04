package echopen.emulator.probe;

import java.util.HashMap;

public class UltrasoundImageSequence {

	private HashMap<Integer, UltrasoundImage> mImageSequence;
	private short mNbPixelsPerLine;
	private short mNbLinesPerImage;

	public UltrasoundImageSequence(short iNbPixelsPerLine, short iNbLinesPerImage){
		mNbPixelsPerLine = iNbPixelsPerLine;
		mNbLinesPerImage = iNbLinesPerImage;
		mImageSequence = new HashMap<>();
	}

	public void addImage(Integer iImageIndex, short[] iPixelValues) {
    mImageSequence.put(iImageIndex, new UltrasoundImage(iPixelValues, mNbPixelsPerLine, mNbLinesPerImage));
	}

	public HashMap<Integer, UltrasoundImage> getImages(){
		return mImageSequence;
	}

	public short getNbPixelsPerLine() {
		return mNbPixelsPerLine;
	}

	public short getNbLinesPerImage() {
		return mNbLinesPerImage;
	}


}

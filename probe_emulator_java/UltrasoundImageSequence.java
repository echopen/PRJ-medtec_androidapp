import java.util.LinkedList;

public class UltrasoundImageSequence {
	 
	private LinkedList<UltrasoundImage> mImageSequence;
	private short mNbPixelsPerLine;
	private short mNbLinesPerImage;
	
	public UltrasoundImageSequence(short iNbPixelsPerLine, short iNbLinesPerImage){
		mNbPixelsPerLine = iNbPixelsPerLine;
		mNbLinesPerImage = iNbLinesPerImage;
		mImageSequence = new LinkedList<>();
	}
	
	public void addImage(short[] iPixelValues) {
		mImageSequence.add(new UltrasoundImage(iPixelValues, mNbPixelsPerLine, mNbLinesPerImage));
	}
	
	public LinkedList<UltrasoundImage> getImages(){
		return mImageSequence;
	}
	
	public short getNbPixelsPerLine() {
		return mNbPixelsPerLine;
	}

	public short getNbLinesPerImage() {
		return mNbLinesPerImage;
	}
	
	
}

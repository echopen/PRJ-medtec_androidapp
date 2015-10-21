package com.echopen.asso.echopen.model.EchoImage;

import android.graphics.Bitmap;

/**
 * Created by mehdibenchoufi on 21/10/15.
 */
public class EchoCharImage extends AbstractEchoImage {

    private char[] pixelsArray;

    private Bitmap bitmap;

    public char[] getPixelsArray() {
        return pixelsArray;
    }

    public void setPixelsArray(char[] pixelsArray) {
        this.pixelsArray = pixelsArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public EchoCharImage(char[] pixelsArray) {
        bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        this.pixelsArray = pixelsArray;
    }

    protected Bitmap accept(Bitmap bitmap){
        Bitmap echoImageToBitmap = new EchoImageToBitmap().visit(this);
        return echoImageToBitmap;
    }
}

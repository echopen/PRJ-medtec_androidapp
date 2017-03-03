package com.echopen.asso.echopen.model.EchoImage;

import android.graphics.Bitmap;

import com.echopen.asso.echopen.utils.Constants;

/**
 * Created by mehdibenchoufi on 21/10/15.
 */
public class EchoIntImage extends AbstractEchoImage {

    private int[] pixelsArray;

    private Bitmap bitmap;

    public int[] getPixelsArray() {
        return pixelsArray;
    }

    public void setPixelsArray(int[] pixelsArray) {
        this.pixelsArray = pixelsArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public EchoIntImage(int[] pixelsArray) {
        bitmap = Bitmap.createBitmap(Constants.PreProcParam.N_z, Constants.PreProcParam.N_x, Bitmap.Config.ARGB_8888);
        this.pixelsArray = pixelsArray;
    }

    protected Bitmap accept(Bitmap bitmap){
        Bitmap echoImageToBitmap = new EchoImageToBitmap().visit(this);
        return echoImageToBitmap;
    }
}

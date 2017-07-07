package com.echopen.asso.echopen.model.EchoImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;

/**
 * Created by mehdibenchoufi on 21/10/15.
 */
public class EchoImageToBitmap  implements EchoImageVisitor{

    public Bitmap visit(EchoIntImage echoIntImage) {
        Bitmap bitmap = echoIntImage.getBitmap();
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(echoIntImage.getPixelsArray()));
        return bitmap;
    }

    public Bitmap visit(EchoCharImage echoCharImage) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = new String(echoCharImage.getPixelsArray()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = getBitmapFromByte(byteArray);
        return bitmap;
    }

    private Bitmap getBitmapFromByte(byte[] byteArray) {
        BitmapFactory.Options thumbOpts = new BitmapFactory.Options();
        thumbOpts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, thumbOpts);
        return bitmap;
    }
}

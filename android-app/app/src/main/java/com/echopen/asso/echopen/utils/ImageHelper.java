package com.echopen.asso.echopen.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by mehdibenchoufi on 20/10/15.
 */
public class ImageHelper {

    /*
    * Filter Bitmap image to display in GrayScale tone
    */
    public static Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /*
    * Image are given with int array of pixels. This pixels int array to pixels byte array
    */
    public byte[] integersToBytes(int[] values) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for(int i=0; i < values.length; ++i)
        {
            dos.writeInt(values[i]);
        }
        return baos.toByteArray();
    }

    /*
    * This method is used to dump UDP data into the int array that will be scan converted
    */
    public static int[] intArrayConcat(int[] o1, int[] o2) {
        int[] ret = new int[o1.length + o2.length];

        System.arraycopy(o1, 0, ret, 0, o1.length);
        System.arraycopy(o2, 0, ret, o1.length, o2.length);

        return ret;
    }

    public static int[] convert(byte bufArray[]) {
        int intArray[] = new int[bufArray.length / 4];
        int off_set = 0;
        long tmp = 0;
        for (int i = 0; i < intArray.length; i++) {
            tmp = (bufArray[3 + off_set] & 0xFF) << 24 | ((bufArray[2 + off_set] & 0xFF) << 16) |
                    ((bufArray[1 + off_set] & 0xFF) << 8) | ((bufArray[0 + off_set] & 0xFF));
            off_set = off_set + 4;
            intArray[i] = (int) (tmp >> 8);
        }
        return intArray;
    }
}
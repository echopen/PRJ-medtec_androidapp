package com.echopen.asso.echopen.utils;

import java.nio.ByteBuffer;

/**
 * Created by mehdibenchoufi on 22/02/17.
 */

public class TypeConverter {

    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}

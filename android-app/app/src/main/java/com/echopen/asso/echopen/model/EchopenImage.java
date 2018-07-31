package com.echopen.asso.echopen.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.UUID;

public class EchopenImage implements Parcelable {
    private UUID mId;
    private Bitmap mBitmap;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public EchopenImage createFromParcel(Parcel in) {
            return new EchopenImage(in);
        }

        public EchopenImage[] newArray(int size) {
            return new EchopenImage[size];
        }
    };

    public EchopenImage(Bitmap iBitmap){
        mBitmap = iBitmap;
        mId = UUID.randomUUID();
    }

    public EchopenImage(Parcel in) {
        mId = in.readParcelable(UUID.class.getClassLoader());
        mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mBitmap);
        dest.writeValue(mId);
    }

    public UUID getId() {
        return mId;
    }
}

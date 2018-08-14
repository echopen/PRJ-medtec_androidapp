package com.echopen.asso.echopen.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class EchopenImageSequence implements Parcelable {
    private ArrayList<EchopenImage> mImageList;
    private UUID mID;

    public EchopenImageSequence(){
        mImageList = new ArrayList<>();
        mID = UUID.randomUUID();
    }

    protected EchopenImageSequence(Parcel in) {
        mImageList = new ArrayList<>();
        in.readTypedList(mImageList, EchopenImage.CREATOR);
        in.readParcelable(UUID.class.getClassLoader());
    }

    public void addImage(EchopenImage iImage){
        mImageList.add(iImage);
    }

    public static final Creator<EchopenImageSequence> CREATOR = new Creator<EchopenImageSequence>() {
        @Override
        public EchopenImageSequence createFromParcel(Parcel in) {
            return new EchopenImageSequence(in);
        }

        @Override
        public EchopenImageSequence[] newArray(int size) {
            return new EchopenImageSequence[size];
        }
    };

    public int getSequenceSize(){
        return mImageList.size();
    }

    public EchopenImage getImage(int iIndex){
       return mImageList.get(iIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mImageList);
        dest.writeValue(mID);
    }
}

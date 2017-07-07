package com.echopen.asso.echopen.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gary on 07/07/2017.
 */

public class Seance implements Parcelable {

    private String name;
    private String description;

    public Seance(String title, String description) {
        this.name = title;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected Seance(Parcel in) {
        name = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Seance> CREATOR = new Parcelable.Creator<Seance>() {
        @Override
        public Seance createFromParcel(Parcel in) {
            return new Seance(in);
        }

        @Override
        public Seance[] newArray(int size) {
            return new Seance[size];
        }
    };
}

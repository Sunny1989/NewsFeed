package com.app.newsfeed.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class SourcePojo implements Parcelable {
    public String id;
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public SourcePojo() {
    }

    protected SourcePojo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SourcePojo> CREATOR = new Parcelable.Creator<SourcePojo>() {
        @Override
        public SourcePojo createFromParcel(Parcel source) {
            return new SourcePojo(source);
        }

        @Override
        public SourcePojo[] newArray(int size) {
            return new SourcePojo[size];
        }
    };
}

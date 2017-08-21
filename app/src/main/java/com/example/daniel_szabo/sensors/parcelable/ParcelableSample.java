package com.example.daniel_szabo.sensors.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableSample implements Parcelable {

    private final long time;
    private final double value;

    public ParcelableSample(long time, double value) {
        this.time = time;
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeDouble(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ParcelableSample> CREATOR = new Creator<ParcelableSample>() {
        @Override
        public ParcelableSample createFromParcel(Parcel source) {
            long time = source.readLong();
            double value = source.readDouble();
            return new ParcelableSample(time, value);
        }

        @Override
        public ParcelableSample[] newArray(int size) {
            return new ParcelableSample[size];
        }
    };
}

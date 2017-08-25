package com.example.daniel_szabo.sensors.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ParcelableSample implements Parcelable, Serializable {

    private final long time;
    private final double x;
    private final double y;
    private final double z;

    public ParcelableSample(long time, double x, double y, double z) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getTime() {
        return time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeDouble(x);
        dest.writeDouble(y);
        dest.writeDouble(z);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ParcelableSample> CREATOR = new Creator<ParcelableSample>() {
        @Override
        public ParcelableSample createFromParcel(Parcel source) {
            long time = source.readLong();
            double x = source.readDouble();
            double y = source.readDouble();
            double z = source.readDouble();
            return new ParcelableSample(time, x, y, z);
        }

        @Override
        public ParcelableSample[] newArray(int size) {
            return new ParcelableSample[size];
        }
    };
}

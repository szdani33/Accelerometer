package com.example.daniel_szabo.sensors;

import android.hardware.Sensor;

public class AccelerometerActivity extends RecordingActivity {

    public AccelerometerActivity() {
        super(Sensor.TYPE_LINEAR_ACCELERATION);
    }
}

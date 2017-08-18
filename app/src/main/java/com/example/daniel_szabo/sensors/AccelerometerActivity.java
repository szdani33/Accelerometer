package com.example.daniel_szabo.sensors;

import android.hardware.Sensor;

public class AccelerometerActivity extends SensorDataRecorderActivity {

    public AccelerometerActivity() {
        super(Sensor.TYPE_LINEAR_ACCELERATION);
    }
}

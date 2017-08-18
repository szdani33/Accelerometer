package com.example.daniel_szabo.sensors;

import android.hardware.Sensor;

public class MagnetometerActivity extends SensorDataRecorderActivity {

    public MagnetometerActivity() {
        super(Sensor.TYPE_MAGNETIC_FIELD);
    }
}

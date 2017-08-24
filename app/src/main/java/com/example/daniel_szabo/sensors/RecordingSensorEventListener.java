package com.example.daniel_szabo.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;

import java.util.LinkedList;
import java.util.List;

public class RecordingSensorEventListener implements SensorEventListener {
    private static final long SECOND_IN_NANOSECONDS = 1000000000;

    private final List<ParcelableSample> recordedData;
    private final int sensorType;
    private final int samplesPerSecond;

    private long firstSensorEventTime;

    public RecordingSensorEventListener(int sensorType, int samplesPerSecond) {
        this.sensorType = sensorType;
        this.samplesPerSecond = samplesPerSecond;
        this.recordedData = new LinkedList<>();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == sensorType && event.timestamp - firstSensorEventTime >= recordedData.size() * SECOND_IN_NANOSECONDS / samplesPerSecond) {
            recordData(event);
        }
        if (firstSensorEventTime == 0) {
            firstSensorEventTime = event.timestamp;
        }
    }

    public List<ParcelableSample> getRecordedData() {
        return recordedData;
    }

    private void recordData(SensorEvent sensorEvent) {
        double x = sensorEvent.values[0];
        double y = sensorEvent.values[1];
        double z = sensorEvent.values[2];
        recordedData.add(new ParcelableSample(sensorEvent.timestamp, x, y, z));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}

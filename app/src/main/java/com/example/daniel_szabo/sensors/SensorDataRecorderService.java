package com.example.daniel_szabo.sensors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;

import java.util.LinkedList;
import java.util.List;

public class SensorDataRecorderService extends Service {

    private static final int SAMPLE_PER_SECOND = 3;
    private static final int SENSOR_TYPE = Sensor.TYPE_LINEAR_ACCELERATION;

    private SensorManager sensorManager;
    private Sensor sensor;

    private boolean recordingEnabled;
    private List<ParcelableSample> recordedData = new LinkedList<>();
    private RecordingSensorEventListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        setupSensor();
    }

    public void startRecording() {
        listener = new RecordingSensorEventListener(SENSOR_TYPE, SAMPLE_PER_SECOND);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        recordingEnabled = true;
    }

    public void stopRecording() {
        sensorManager.unregisterListener(listener);
        recordingEnabled = false;
        recordedData = listener.getRecordedData();
    }

    public List<ParcelableSample> getRecordedData() {
        return recordedData;
    }

    public boolean isRecordingEnabled() {
        return recordingEnabled;
    }

    public boolean hasRecordedData() {
        return !recordedData.isEmpty();
    }

    private void setupSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(SENSOR_TYPE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

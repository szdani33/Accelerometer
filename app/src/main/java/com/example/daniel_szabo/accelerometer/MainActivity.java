package com.example.daniel_szabo.accelerometer;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int SAMPLE_PER_SECOND = 60;
    private static final List<Pair<Long, Double>> data = new LinkedList<>();

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastSampleTime = 0;
    private boolean recordingEnabled;

    private Button startButton;
    private Button stopButton;
    private Button showButton;

    float x;
    float y;
    float z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setupButtons();
        setupSensor();
    }

    private void setupButtons() {
        startButton = (Button) findViewById(R.id.button_start);
        startButton.setEnabled(!recordingEnabled);
        stopButton = (Button) findViewById(R.id.button_stop);
        stopButton.setEnabled(recordingEnabled);
        showButton = (Button) findViewById(R.id.button_show);
        showButton.setEnabled(recordingEnabled);
    }

    private void setupSensor() {
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        long currentTime = System.currentTimeMillis();
        if (shouldRecordData(mySensor, currentTime)) {
            recordData(sensorEvent, currentTime);
        }
    }

    private void recordData(SensorEvent sensorEvent, long currentTime) {
        lastSampleTime = System.currentTimeMillis();
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];
        double g = Math.sqrt(x * x + y * y + z * z) / 8.91f;
        double roundedG = new BigDecimal(g).setScale(2, RoundingMode.HALF_UP).doubleValue();
        data.add(new Pair(currentTime, roundedG));
    }

    public void startRecording(View view) {
        data.clear();
        changeRecordingStatus();
    }

    public void stopRecording(View view) {
        changeRecordingStatus();
    }

    private void changeRecordingStatus() {
        recordingEnabled = !recordingEnabled;
        startButton.setEnabled(!startButton.isEnabled());
        stopButton.setEnabled(!stopButton.isEnabled());
        showButton.setEnabled(!recordingEnabled && !data.isEmpty());
    }

    private boolean shouldRecordData(Sensor mySensor, long currentTime) {
        return recordingEnabled && mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && currentTime - lastSampleTime > 1000 / SAMPLE_PER_SECOND;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void showGraph(View view) {
        startActivity(new Intent(this, GraphActivity.class));
    }

    public static List<Pair<Long, Double>> getData() {
        return data;
    }
}

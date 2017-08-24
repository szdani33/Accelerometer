package com.example.daniel_szabo.sensors;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class SensorDataRecorderActivity extends AppCompatActivity {
    public static final String RECORDED_DATA = "recordedDataKey";
    public static final String DATA_TYPE_NAME = "graphNameKey";

    private Button startButton;
    private Button stopButton;
    private Button showButton;
    private SensorDataRecorderService service;
    private TextView serviceStatusTV;
    private TextView recordingStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data_recorder);
        initComponents();
        bindService();
    }

    private void bindService() {
        bindService(new Intent(this, SensorDataRecorderService.class), new SimpleServiceConnection(), BIND_AUTO_CREATE);
    }

    private void initComponents() {
        startButton = (Button) findViewById(R.id.startRecordingBt);
        stopButton = (Button) findViewById(R.id.stopRecordingBt);
        showButton = (Button) findViewById(R.id.showGraphBt);

        serviceStatusTV = (TextView) findViewById(R.id.serviceStatusTV);
        recordingStatusTV = (TextView) findViewById(R.id.recordingStatusTV);
    }

    public void startRecording(View view) {
        service.startRecording();
        updateComponents();
    }

    public void stopRecording(View view) {
        service.stopRecording();
        updateComponents();
    }

    public void showGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(DATA_TYPE_NAME, getTitle());
        intent.putParcelableArrayListExtra(RECORDED_DATA, new ArrayList<>(service.getRecordedData()));
        startActivity(intent);
    }

    private void updateComponents() {
        if (service == null) {
            serviceStatusTV.setText(getString(R.string.serviceStatusText) + " " + getString(R.string.serviceDisconnectedText));
            recordingStatusTV.setText(getString(R.string.recordingStatusText) + " " + getString(R.string.recordingStoppedText));
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            showButton.setEnabled(false);
        } else {
            serviceStatusTV.setText(getString(R.string.serviceStatusText) + " " + getString(R.string.serviceCconnectedText));
            recordingStatusTV.setText(getString(R.string.recordingStatusText) + " " + (service.isRecordingEnabled() ? getString(R.string.recordingRunnigText) : getString(R.string.recordingStoppedText)));
            boolean recordingEnabled = service.isRecordingEnabled();
            startButton.setEnabled(!recordingEnabled);
            stopButton.setEnabled(recordingEnabled);
            showButton.setEnabled(!recordingEnabled && service.hasRecordedData());
        }
    }

    private class SimpleServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((ServiceBinder<SensorDataRecorderService>) binder).getService();
            updateComponents();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            updateComponents();
        }
    }
}

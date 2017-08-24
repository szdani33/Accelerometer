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
    private TextView statusTV;
    private String statusTVText;

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
        startButton = (Button) findViewById(R.id.button_start);
        stopButton = (Button) findViewById(R.id.button_stop);
        showButton = (Button) findViewById(R.id.button_show);
        statusTV = (TextView) findViewById(R.id.statusTV);
        statusTVText = statusTV.getText().toString();
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
        boolean recordingEnabled = service.isRecordingEnabled();
        startButton.setEnabled(!recordingEnabled);
        stopButton.setEnabled(recordingEnabled);
        showButton.setEnabled(!recordingEnabled && service.hasRecordedData());

        statusTV.setText(statusTVText + " " + getServiceStatusText());
    }

    private String getServiceStatusText() {
        if(service == null) {
            return ServiceConnectionStatus.DISCONNECTED.name();
        } else if (service.isRecordingEnabled()) {
            return ServiceConnectionStatus.RUNNING.name();
        } else {
            return ServiceConnectionStatus.CONNECTED.name();
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

    private enum ServiceConnectionStatus {
        DISCONNECTED,
        CONNECTED,
        RUNNING;
    }
}

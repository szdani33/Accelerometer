package com.example.daniel_szabo.sensors;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.ArrayList;

public abstract class SensorDataRecorderActivity extends AppCompatActivity {
    public static final String RECORDED_DATA = "recordedDataKey";
    public static final String DATA_TYPE_NAME = "graphNameKey";

    private ToggleButton startStopButton;
    private Button showButton;
    private SensorDataRecorderService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data_recorder);
        initComponents();
        bindService(new Intent(this, SensorDataRecorderService.class), new SimpleServiceConnection(), BIND_AUTO_CREATE);
    }

    private void initComponents() {
        startStopButton = (ToggleButton) findViewById(R.id.startStopTB);
        showButton = (Button) findViewById(R.id.showGraphBt);
    }

    public void startStopRecording(View view) {
        if (startStopButton.isChecked()) {
            service.startRecording();
        } else {
            service.stopRecording();
        }
        updateButtons();
    }

    public void showGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(DATA_TYPE_NAME, getTitle());
        intent.putParcelableArrayListExtra(RECORDED_DATA, new ArrayList<>(service.getRecordedData()));
        startActivity(intent);
    }

    private void updateButtons() {
        boolean recordingEnabled = service.isRecordingEnabled();
        showButton.setEnabled(!recordingEnabled && service.hasRecordedData());
    }

    private class SimpleServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((ServiceBinder<SensorDataRecorderService>) binder).getService();
            updateButtons();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

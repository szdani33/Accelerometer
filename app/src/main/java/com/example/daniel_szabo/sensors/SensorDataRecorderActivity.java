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

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class SensorDataRecorderActivity extends AppCompatActivity {
    public static final String RECORDED_DATA = "recordedDataKey";
    public static final String DATA_TYPE_NAME = "graphNameKey";

    private List<ParcelableSample> recordedData;

    private Button startRecordingBt;
    private Button stopRecordingBt;
    private Button showGraphBt;
    private Button saveDataBt;
    private Button loadDataBt;
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
        startRecordingBt = (Button) findViewById(R.id.startRecordingBt);
        stopRecordingBt = (Button) findViewById(R.id.stopRecordingBt);
        showGraphBt = (Button) findViewById(R.id.showGraphBt);
        saveDataBt = (Button) findViewById(R.id.saveDataBt);
        loadDataBt = (Button) findViewById(R.id.loadDataBt);

        serviceStatusTV = (TextView) findViewById(R.id.serviceStatusTV);
        recordingStatusTV = (TextView) findViewById(R.id.recordingStatusTV);
    }

    public void startRecording(View view) {
        service.startRecording();
        updateComponents();
    }

    public void stopRecording(View view) {
        service.stopRecording();
        recordedData = service.getRecordedData();
        updateComponents();
    }

    public void showGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(DATA_TYPE_NAME, getTitle());
        intent.putParcelableArrayListExtra(RECORDED_DATA, new ArrayList<>(recordedData));
        startActivity(intent);
    }

    private void updateComponents() {
        if (service == null) {
            serviceStatusTV.setText(getString(R.string.serviceStatusText) + " " + getString(R.string.serviceDisconnectedText));
            recordingStatusTV.setText(getString(R.string.recordingStatusText) + " " + getString(R.string.recordingStoppedText));
            startRecordingBt.setEnabled(false);
            stopRecordingBt.setEnabled(false);
            showGraphBt.setEnabled(false);
            saveDataBt.setEnabled(false);
            loadDataBt.setEnabled(true);
        } else {
            serviceStatusTV.setText(getString(R.string.serviceStatusText) + " " + getString(R.string.serviceCconnectedText));
            recordingStatusTV.setText(getString(R.string.recordingStatusText) + " " + (service.isRecordingEnabled() ? getString(R.string.recordingRunnigText) : getString(R.string.recordingStoppedText)));
            boolean recordingEnabled = service.isRecordingEnabled();
            startRecordingBt.setEnabled(!recordingEnabled);
            stopRecordingBt.setEnabled(recordingEnabled);
            showGraphBt.setEnabled(hasRecordedData());
            saveDataBt.setEnabled(hasRecordedData());
            loadDataBt.setEnabled(!recordingEnabled);
        }
    }

    public void saveData(View view) {
        try {
            FileOutputStream fos = openFileOutput("savedData", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(service.getRecordedData());
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(View view) {
        try {
            FileInputStream fis = openFileInput("savedData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            recordedData = (List<ParcelableSample>) ois.readObject();
            fis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        updateComponents();
    }

    private boolean hasRecordedData() {
        return recordedData != null && !recordedData.isEmpty();
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

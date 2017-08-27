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
import android.widget.Toast;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;
import com.example.daniel_szabo.sensors.util.FileUtil;
import com.example.daniel_szabo.sensors.util.LargeDataTransferUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SensorDataRecorderActivity extends AppCompatActivity {
    public static final String SAVED_DATA_FILE_NAME = "savedData";

    private List<ParcelableSample> recordedData;

    private Button startRecordingBt;
    private Button stopRecordingBt;
    private Button showGraphBt;
    private Button saveDataBt;
    private Button loadDataBt;
    private SensorDataRecorderService service;
    private TextView serviceStatusTV;
    private TextView recordingStatusTV;
    private SimpleServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data_recorder);
        initComponents();
        bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateComponents();
    }

    private void bindService() {
        serviceConnection = new SimpleServiceConnection();
        bindService(new Intent(this, SensorDataRecorderService.class), serviceConnection, BIND_AUTO_CREATE);
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
        Intent intent;
//        recordedData = createLargeData(1001);
        if (recordedData.size() <= 1000) {
            intent = createGraphActivityIntent();
        } else {
            intent = createGraphActivityIntentForLargeData();
        }
        startActivity(intent);
    }

    private Intent createGraphActivityIntent() {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putParcelableArrayListExtra(GraphActivity.RECORDED_DATA_INTENT_KEY, new ArrayList<>(recordedData));
        return intent;
    }

    private Intent createGraphActivityIntentForLargeData() {
        Intent intent = new Intent(this, FileReadingGraphActivity.class);
        try {
            LargeDataTransferUtil.writeDataToTempFile(getApplicationContext(), FileReadingGraphActivity.RECORDED_DATA_FILE_NAME_INTENT_KEY, new ArrayList<>(recordedData));
        } catch (IOException e) {
            toastMessage("Cannot open Graph!");
        }
        return intent;
    }

//    private List<ParcelableSample> createLargeData(int amount) {
//        Random r = new Random();
//        List<ParcelableSample> result = new ArrayList<>();
//        for (int i = 0; i < amount; i++) {
//            result.add(new ParcelableSample(i * 60, r.nextDouble(), r.nextDouble(), r.nextDouble()));
//        }
//        return result;
//    }

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
            FileUtil.writeObjectToFile(openFileOutput(SAVED_DATA_FILE_NAME, MODE_PRIVATE), new ArrayList<>(recordedData));
            toastMessage("Data saved successfully.");
        } catch (IOException e) {
            toastMessage("Could NOT save data!");
        }
    }

    public void loadData(View view) {
        try {
            recordedData = FileUtil.readObjectFromFile(openFileInput(SAVED_DATA_FILE_NAME));
            toastMessage("Data loaded succesfully.");
        } catch (FileNotFoundException e) {
            toastMessage("No data found!");
        } catch (IOException e) {
            toastMessage("Could NOT load data!");
        }
        updateComponents();
    }

    private void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private boolean hasRecordedData() {
        return recordedData != null && !recordedData.isEmpty();
    }

    private class SimpleServiceConnection implements ServiceConnection {
        @Override
        @SuppressWarnings("unchecked")
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

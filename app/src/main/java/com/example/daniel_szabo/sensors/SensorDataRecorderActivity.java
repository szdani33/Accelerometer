package com.example.daniel_szabo.sensors;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;

import java.util.ArrayList;
import java.util.List;

public abstract class SensorDataRecorderActivity extends AppCompatActivity /*implements SensorEventListener */{
    public static final String RECORDED_DATA = "recordedDataKey";
    public static final String DATA_TYPE_NAME = "graphNameKey";

//    private static final int SAMPLE_PER_SECOND = 60;
//    private static final double FRAME_LENGTH = 1000 / SAMPLE_PER_SECOND;
//    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss.S");

//    private List<ParcelableSample> data = new LinkedList<>();
//    private TextView startedTextView;
//    private TextView samplesTextView;
//    private String startedText;
//    private String samplesText;

//    private SensorManager senSensorManager;
//    private Sensor senAccelerometer;
//    private long startTime;

    private Button startButton;
    private Button stopButton;
    private Button showButton;
    private SensorDataRecorderService service;
    private List<ParcelableSample> recordedData;

//    private final int sensorType;

//    public SensorDataRecorderActivity(int sensorType) {
//        this.sensorType = sensorType;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data_recorder);
        setupComponents();
        bindService(new Intent(this, SensorDataRecorderService.class), new SimpleServiceConnection(), BIND_AUTO_CREATE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        setupSensor();
    }

    private void setupComponents() {
        startButton = (Button) findViewById(R.id.button_start);
        stopButton = (Button) findViewById(R.id.button_stop);
        showButton = (Button) findViewById(R.id.button_show);

//        samplesTextView = (TextView) findViewById(R.id.sample_count);
//        samplesText = samplesTextView.getText().toString();
//        startedTextView = (TextView) findViewById(R.id.started);
//        startedText = startedTextView.getText().toString();
//        updateTexts(0, DATE_FORMAT);
    }

    public void startRecording(View view) {
        service.startRecording();
        updateButtons();
//        data.clear();
//        changeRecordingStatus();
//        startTime = System.currentTimeMillis();
    }

    public void stopRecording(View view) {
        service.stopRecording();
        updateButtons();
//        changeRecordingStatus();
//        updateTexts(System.currentTimeMillis() - startTime, new SimpleDateFormat("mm:ss.SSS"));
    }

    public void showGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(DATA_TYPE_NAME, getTitle());
        intent.putParcelableArrayListExtra(RECORDED_DATA, new ArrayList<>(service.getRecordedData()));
        startActivity(intent);
    }

    private void updateButtons() {
        boolean recordingEnabled = service.isRecordingEnabled();
        startButton.setEnabled(!recordingEnabled);
        stopButton.setEnabled(recordingEnabled);
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

//    private void setupSensor() {
//        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        senAccelerometer = senSensorManager.getDefaultSensor(sensorType);
//        registerSensor();
//    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        Sensor mySensor = sensorEvent.sensor;
//        long currentTime = System.currentTimeMillis();
//        if(recordingEnabled) {
//            if (shouldRecordData(mySensor, currentTime)) {
//                recordData(sensorEvent, currentTime);
//            }
//            updateTexts(currentTime - startTime, DATE_FORMAT);
//        }
//    }

//    private void updateTexts(long elapsedTime, DateFormat dateFormat) {
//        startedTextView.setText(startedText + " " + dateFormat.format(new Date(elapsedTime)));
//        samplesTextView.setText(samplesText + " " + data.size());
//    }

//    private void recordData(SensorEvent sensorEvent, long currentTime) {
//        double x = sensorEvent.values[0];
//        double y = sensorEvent.values[1];
//        double z = sensorEvent.values[2];
//        double value = Math.sqrt(x * x + y * y + z * z) / 8.91f;
//        double roundedValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
//        data.add(new ParcelableSample(currentTime, roundedValue));
//    }

//    private void changeRecordingStatus() {
//        recordingEnabled = !recordingEnabled;
//        startButton.setEnabled(!startButton.isEnabled());
//        stopButton.setEnabled(!stopButton.isEnabled());
//        showButton.setEnabled(!recordingEnabled && !data.isEmpty());
//    }

//    private boolean shouldRecordData(Sensor mySensor, long currentTime) {
//        return mySensor.getType() == sensorType && currentTime - startTime > data.size() * FRAME_LENGTH;
//    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//    }

//    private void unregisterSensor() {
//        senSensorManager.unregisterListener(this);
//    }

//    private void registerSensor() {
//        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterSensor();
//    }
}

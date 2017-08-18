package com.example.daniel_szabo.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MagnetometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senMagnetometer;
    private String strengthText;
    private TextView magneticFieldTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senMagnetometer = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senSensorManager.registerListener(this, senMagnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        magneticFieldTV = (TextView) findViewById(R.id.magneticFieldTV);
        strengthText = magneticFieldTV.getText().toString();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            double value = new BigDecimal(event.values[0]).setScale(2, RoundingMode.HALF_UP).doubleValue();
            magneticFieldTV.setText(strengthText + " " + value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        senSensorManager.unregisterListener(this);
    }
}

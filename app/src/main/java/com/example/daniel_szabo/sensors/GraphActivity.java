package com.example.daniel_szabo.sensors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    public static final String RECORDED_DATA_INTENT_KEY = "recordedDataIntentKey";
    public static final String ACTIVITY_TITLE = "Accelerometer Graph";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle(ACTIVITY_TITLE);
        setupGraph(loadRawData());
    }

    protected List<ParcelableSample> loadRawData() {
        return getIntent().getParcelableArrayListExtra(RECORDED_DATA_INTENT_KEY);
    }

    private void setupGraph(List<ParcelableSample> rawData) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = createSeries(rawData);
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setMaxX(series.getHighestValueX());
        graph.getViewport().setMaxY(Math.max(series.getHighestValueY() * 1.05, 1));
    }

    private LineGraphSeries<DataPoint> createSeries(List<ParcelableSample> rawData) {
        List<DataPoint> dataPoints = new LinkedList<>();
        if (rawData != null &&!rawData.isEmpty()) {
            long firstTime = rawData.get(0).getTime();
            for (ParcelableSample d : rawData) {
                dataPoints.add(createDataPoint(firstTime, d));
            }
        }
        return new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
    }

    @NonNull
    private DataPoint createDataPoint(long firstTime, ParcelableSample sample) {
        double x = sample.getX();
        double y = sample.getY();
        double z = sample.getZ();
        double value = Math.sqrt(x * x + y * y + z * z) / 8.91f;
        double roundedValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new DataPoint(sample.getTime() - firstTime, roundedValue);
    }
}
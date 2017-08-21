package com.example.daniel_szabo.sensors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private ArrayList<ParcelableSample> rawRecordedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle(getIntent().getStringExtra(SensorDataRecorderActivity.DATA_TYPE_NAME) + " Graph");
        rawRecordedData = getIntent().getParcelableArrayListExtra(SensorDataRecorderActivity.RECORDED_DATA);
        setupGraph();
    }

    private void setupGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = createSeries();
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

    private LineGraphSeries<DataPoint> createSeries() {
        List<DataPoint> dataPoints = new LinkedList<>();
        if (!rawRecordedData.isEmpty()) {
            long firstTime = rawRecordedData.get(0).getTime();
            for (ParcelableSample d : rawRecordedData) {
                dataPoints.add(new DataPoint(d.getTime() - firstTime, d.getValue()));
            }
        }
        return new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
    }
}

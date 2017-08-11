package com.example.daniel_szabo.accelerometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private static final int RANGE = 3000;

    double maxG = 0;

    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setupGraph();
    }

    private void setupGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(createSeries());

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5*RANGE);
        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        graph.addSeries(this.series);
    }

    private LineGraphSeries<DataPoint> createSeries() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(createDataArray());

        return series;
    }

    private DataPoint[] createDataArray() {
        List<Pair<Long, Double>> data = MainActivity.getData();
        List<DataPoint> dataPoints = new LinkedList<>();
        if (!data.isEmpty()) {
            long firstTime = data.get(0).first;
            for (Pair<Long, Double> d : data) {
                dataPoints.add(new DataPoint(d.first - firstTime, d.second));
            }
        }
        return new DataPoint[dataPoints.size()];
    }
}

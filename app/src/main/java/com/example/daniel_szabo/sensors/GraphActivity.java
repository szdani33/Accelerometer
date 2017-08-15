package com.example.daniel_szabo.sensors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    private double maxG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setupGraph();
    }

    private void setupGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        setupSeries();
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

    private void setupSeries() {
        List<Pair<Long, Double>> data = AccelerometerActivity.getData();
        List<DataPoint> dataPoints = new LinkedList<>();
        if (!data.isEmpty()) {
            long firstTime = data.get(0).first;
            for (Pair<Long, Double> d : data) {
                dataPoints.add(new DataPoint(d.first - firstTime, d.second));
            }
        }
        series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
    }
}

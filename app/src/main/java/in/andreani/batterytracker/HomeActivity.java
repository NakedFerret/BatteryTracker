package in.andreani.batterytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.logging.Logger;

public class HomeActivity extends AppCompatActivity {

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        chart = (LineChart) findViewById(R.id.chart);
        LineDataSet dataSet = new LineDataSet(this.getData(), "Battery Level");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    private ArrayList<Entry> getData() {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (String row : Data.FAKE_BATTERY_DATA) {
            // Event, Value, Time
            String[] columns = row.split(",");
            if (!columns[0].equals("Battery")) {
                continue;
            }

            Float time = Float.valueOf(columns[2]);
            Float value = Float.valueOf(columns[1]);

            Log.i(getApplication().getPackageName(), "data input: " + columns[2] + ' ' + columns[1]);
            Log.i(getApplication().getPackageName(), "data point: " + time.toString() + ' ' + value.toString());
            entries.add(new Entry(time, value));
        }

        return entries;
    }
}

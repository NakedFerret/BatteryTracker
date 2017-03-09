package in.andreani.batterytracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.andreani.batterytracker.io.CSVExporter;
import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Gonzalo Andreani on 2/26/17.
 */

public class HomeActivity extends AppCompatActivity {

    private LineChart chart;
    private Button exportButton;
    private CSVExporter csvExporter;
    private CSVExportTask exportTask;
    private ChartLoadingTask chartLoadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        csvExporter = new CSVExporter(getExternalFilesDir(null));

        exportButton = (Button) findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.exportTask = new CSVExportTask();
                HomeActivity.this.exportTask.execute();
            }
        });

        chart = (LineChart) findViewById(R.id.chart);
        chartLoadTask = new ChartLoadingTask();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chartLoadTask != null) {
            chartLoadTask.cancel(true);
        }
        chartLoadTask = new ChartLoadingTask();
        chartLoadTask.execute();
    }

    private class ChartLoadingTask extends AsyncTask<Void, Void, List<Entry>> {

        @Override
        protected List<Entry> doInBackground(Void... params) {
            Realm realm = Realm.getDefaultInstance();

            RealmResults<LogRecord> sortedMaxBatteries = realm.where(LogRecord.class)
                    .equalTo("type", "Battery")
                    .equalTo("value", 100L)
                    .findAllSorted("time", Sort.DESCENDING);

            RealmQuery<LogRecord> query = realm.where(LogRecord.class);

            if (!sortedMaxBatteries.isEmpty()) {
                sortedMaxBatteries.load();
                LogRecord lastFull = sortedMaxBatteries.first();
                query.greaterThanOrEqualTo("time", lastFull.time);
            }

            RealmResults<LogRecord> records = query.findAll();

            records.load();

            List<Entry> entries = new ArrayList<Entry>();

            for (LogRecord record : records) {
                if (!record.type.equals(LogRecord.TYPE_BATTERY))
                    continue;

                entries.add(new Entry(record.time, record.value));
            }

            return entries;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {

            if (entries.isEmpty()) {
                return;
            }

            LineDataSet batteryDataSet = new LineDataSet(entries, "Battery");
            LineData data = new LineData(batteryDataSet);
            HomeActivity.this.chart.setData(data);
            XAxis xAxis = HomeActivity.this.chart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                private DateFormat DATE_FORMAT = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return DATE_FORMAT.format(value);
                }
            });
            HomeActivity.this.chart.invalidate();
        }
    }

    private class CSVExportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Realm realm = Realm.getDefaultInstance();

            try {
                RealmResults<LogRecord> allLogRecords = realm.where(LogRecord.class).findAll();
                allLogRecords.load();
                HomeActivity.this.csvExporter.exportRecords(allLogRecords);
            } finally {
                realm.close();
            }

            return null;
        }
    }

}

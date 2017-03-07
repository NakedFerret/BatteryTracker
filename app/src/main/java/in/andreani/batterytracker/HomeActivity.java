package in.andreani.batterytracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import in.andreani.batterytracker.io.CSVExporter;
import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;
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

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LogRecord> sortedMaxBatteries = realm.where(LogRecord.class)
                        .equalTo("type", "Battery")
                        .equalTo("value", 100L)
                        .findAllSorted("time", Sort.DESCENDING);

                sortedMaxBatteries.load();

                LogRecord lastFull = sortedMaxBatteries.first();

                RealmResults<LogRecord> lastFullReadings = realm.where(LogRecord.class)
                        .greaterThanOrEqualTo("time", lastFull.time)
                        .findAll();
            }
        });

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

package in.andreani.batterytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;

import in.andreani.batterytracker.io.CSVExporter;
import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Gonzalo Andreani on 2/26/17.
 */

public class HomeActivity extends AppCompatActivity {

    private LineChart chart;
    private Button exportButton;
    private CSVExporter csvExporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        csvExporter = new CSVExporter(getExternalFilesDir(null));

        exportButton = (Button) findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.csvExporter.exportRecords();
            }
        });

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        LogRecord screenRecord = LogRecord.getScreenRecord(1);
        LogRecord idleRecord = LogRecord.getIdleRecord(1);

        realm.copyToRealm(screenRecord);
        realm.copyToRealm(idleRecord);

        realm.commitTransaction();

        RealmResults<LogRecord> allRecords = realm.where(LogRecord.class).findAll();

        Log.d(getApplication().getPackageName(), "Found #" + allRecords.size() + " records");
    }


}

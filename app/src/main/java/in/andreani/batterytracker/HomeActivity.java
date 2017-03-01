package in.andreani.batterytracker;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HomeActivity extends AppCompatActivity {

    private LineChart chart;
    private Button exportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        chart = (LineChart) findViewById(R.id.chart);
        LineDataSet dataSet = new LineDataSet(this.getData(), "Battery Level");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        exportButton = (Button) findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.writeTestOuputFile();
            }
        });

    }

    public void writeTestOuputFile() {
        if (this.isExternalStorageMounted()) {
            File file = new File(getExternalFilesDir(null), "battery-tracker-test-file.txt");
            try {
                OutputStream output = new FileOutputStream(file);
                String outputText = "Hello World" + '\n';
                output.write(outputText.getBytes("UTF-8"));
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Could NOT write output file", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isExternalStorageMounted() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
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

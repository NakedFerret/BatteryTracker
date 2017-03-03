package in.andreani.batterytracker.io;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import in.andreani.batterytracker.model.LogRecord;
import io.realm.RealmResults;

/**
 * Created by Gonzalo Andreani on 3/2/17.
 */

public class CSVExporter {

    public static final String EXPORT_CSV_FILENAME = "battery-tracker-log.csv";

    private File outputDir;

    public CSVExporter(File outputDir) {
        this.outputDir = outputDir;
    }

    public void exportRecords(RealmResults<LogRecord> allLogRecords) {
        if (this.isExternalStorageMounted()) {
            File file = new File(this.outputDir, EXPORT_CSV_FILENAME);
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                for (LogRecord record : allLogRecords) {
                    String row = record.type + ',' +
                            record.time + ',' +
                            record.value;
                    output.write(row);
                    output.newLine();
                }
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isExternalStorageMounted() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}

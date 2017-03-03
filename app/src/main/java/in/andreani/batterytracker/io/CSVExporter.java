package in.andreani.batterytracker.io;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Gonzalo Andreani on 3/2/17.
 */

public class CSVExporter {

    public static final String EXPORT_CSV_FILENAME = "battery-tracker-test-file.txt";

    private File outputDir;

    public CSVExporter(File outputDir) {
        this.outputDir = outputDir;
    }

    public void exportRecords() {
        if (this.isExternalStorageMounted()) {
            File file = new File(this.outputDir, EXPORT_CSV_FILENAME);
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                String outputText = "Hello World";
                output.write(outputText);
                output.newLine();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Toast.makeText(this, "Could NOT write output file", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isExternalStorageMounted() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}

package in.andreani.batterytracker.model;

import io.realm.RealmObject;

/**
 * Created by Gonzalo Andreani on 3/2/17.
 */

public class LogRecord extends RealmObject {

    public static final String TYPE_BATTERY = "Battery";
    public static final String TYPE_SCREEN = "Screen";
    public static final String TYPE_IDLE = "Idle";

    public String type;
    public long time;
    public long value;

    public LogRecord() {
    }

    public LogRecord(String type, long time, long value) {
        this.type = type;
        this.time = time;
        this.value = value;
    }

    public static LogRecord getBatteryRecord(long value) {
        return new LogRecord(TYPE_BATTERY, System.currentTimeMillis(), value);
    }

    public static LogRecord getScreenRecord(long value) {
        return new LogRecord(TYPE_SCREEN, System.currentTimeMillis(), value);
    }

    public static LogRecord getIdleRecord(long value) {
        return new LogRecord(TYPE_IDLE, System.currentTimeMillis(), value);
    }

}

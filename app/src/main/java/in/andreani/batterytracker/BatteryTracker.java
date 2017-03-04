package in.andreani.batterytracker;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import in.andreani.batterytracker.receivers.BatteryReceiver;
import io.realm.Realm;

/**
 * Created by Gonzalo Andreani on 3/1/17.
 */
public class BatteryTracker extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BatteryReceiver(), filter);

        Realm.init(this);
    }
}
package in.andreani.batterytracker;

import android.app.Application;
import android.content.Intent;

import in.andreani.batterytracker.services.LoggingService;
import io.realm.Realm;

/**
 * Created by Gonzalo Andreani on 3/1/17.
 */
public class BatteryTracker extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, LoggingService.class));

        Realm.init(this);
    }
}
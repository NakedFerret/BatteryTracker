package in.andreani.batterytracker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import io.realm.Realm;

/**
 * Created by Gonzalo Andreani on 3/1/17.
 */
public class BatteryTracker extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
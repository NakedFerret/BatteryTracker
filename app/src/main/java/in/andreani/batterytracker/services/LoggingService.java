package in.andreani.batterytracker.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import in.andreani.batterytracker.receivers.BatteryReceiver;

public class LoggingService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startBatteryReceiver();
        return START_STICKY;
    }

    private void startBatteryReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BatteryReceiver(), filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

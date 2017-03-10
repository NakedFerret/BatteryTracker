package in.andreani.batterytracker.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import in.andreani.batterytracker.receivers.BatteryReceiver;

public class LoggingService extends Service {

    private BatteryReceiver batteryReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startBatteryReceiver();
        return START_STICKY;
    }

    private void startBatteryReceiver() {
        if (batteryReceiver != null) {
            return;
        }

        batteryReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

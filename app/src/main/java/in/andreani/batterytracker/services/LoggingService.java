package in.andreani.batterytracker.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import in.andreani.batterytracker.receivers.BatteryReceiver;
import in.andreani.batterytracker.receivers.ScreenReceiver;

public class LoggingService extends Service {

    private ScreenReceiver screenReceiver;
    private BatteryReceiver batteryReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startBatteryReceiver();
        startScreenReceiver();
        return START_STICKY;
    }

    private void startScreenReceiver() {
        if (screenReceiver != null) {
            return;
        }

        screenReceiver = ScreenReceiver.registerSelf(this);
    }

    private void startBatteryReceiver() {
        if (batteryReceiver != null) {
            return;
        }

        batteryReceiver = BatteryReceiver.registerSelf(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }

        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

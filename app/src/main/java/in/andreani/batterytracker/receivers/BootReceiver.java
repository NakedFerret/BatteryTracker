package in.andreani.batterytracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import in.andreani.batterytracker.services.LoggingService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LoggingService.class));
    }
}

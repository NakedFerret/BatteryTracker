package in.andreani.batterytracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;

public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {
    }

    public static ScreenReceiver registerSelf(Context context) {
        ScreenReceiver screenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(screenReceiver, filter);
        return screenReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Long screenValue = null;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenValue = 1L;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenValue = 0L;
        }

        if (screenValue == null) {
            return;
        }

        LogRecord record = LogRecord.getScreenRecord(screenValue);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(record);
        realm.commitTransaction();
    }
}

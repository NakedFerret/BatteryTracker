package in.andreani.batterytracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;

public class BatteryReceiver extends BroadcastReceiver {
    public static BatteryReceiver registerSelf(Context context) {
        BatteryReceiver batteryReceiver = new BatteryReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        context.registerReceiver(batteryReceiver, filter);

        return batteryReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        float scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level == -1) {
            return;
        }

        int batteryLeft = (scale == -1) ? (int) level : (int) ((level / scale) * 100);

        LogRecord record = LogRecord.getBatteryRecord(batteryLeft);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(record);
        realm.commitTransaction();
    }
}

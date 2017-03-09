package in.andreani.batterytracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;

public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Realm realm = Realm.getDefaultInstance();

        float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        float scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level == -1) {
            return;
        }

        int batteryLeft = (scale == -1) ? (int) level : (int) ((level / scale) * 100);

        LogRecord record = LogRecord.getBatteryRecord(batteryLeft);

        realm.beginTransaction();
        realm.copyToRealm(record);
        realm.commitTransaction();
    }
}

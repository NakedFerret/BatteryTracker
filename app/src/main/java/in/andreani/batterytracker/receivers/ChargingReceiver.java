package in.andreani.batterytracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import in.andreani.batterytracker.model.LogRecord;
import io.realm.Realm;

public class ChargingReceiver extends BroadcastReceiver {

    public static ChargingReceiver registerSelf(Context context) {
        ChargingReceiver chargingReceiver = new ChargingReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);

        context.registerReceiver(chargingReceiver, filter);

        return chargingReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isCharging = intent.getAction().equals(Intent.ACTION_POWER_CONNECTED);

        long isChargingValue = isCharging ? 1 : 0;

        LogRecord record = LogRecord.getChargingRecord(isChargingValue);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(record);
        realm.commitTransaction();
    }
}

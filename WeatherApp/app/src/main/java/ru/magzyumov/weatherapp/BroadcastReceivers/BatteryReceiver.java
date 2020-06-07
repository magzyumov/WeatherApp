package ru.magzyumov.weatherapp.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.provider.Settings;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.PhoneClass;

import static android.os.BatteryManager.BATTERY_PLUGGED_AC;
import static android.os.BatteryManager.BATTERY_PLUGGED_USB;

public class BatteryReceiver extends BroadcastReceiver implements Constants {
    private int messageId = 1;
    private Context context;
    private DatabaseReference firebaseDB;

    public BatteryReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = context.registerReceiver(null, intentFilter);

        int batteryStatus = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;

        int chargePlug = battery.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BATTERY_PLUGGED_AC;

        sendInformationToServer(isCharging, usbCharge, acCharge, batteryLevel(battery));
    }

    private void sendInformationToServer(boolean isCharging, boolean usbCharge, boolean acCharge,
                                         String batteryLevel){
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.getDefault());
        Date date = new Date();
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String timeStamp = dateFormat.format(date);

        firebaseDB = FirebaseDatabase.getInstance().getReference();
        PhoneClass phoneState = new PhoneClass(timeStamp, isCharging, usbCharge, acCharge, batteryLevel);
        firebaseDB.child(PHONES).child(id).child(BATTERY).setValue(phoneState.getBattery());
    }

    private String batteryLevel(Intent battery){
        int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return String.valueOf(level*100/scale);
    }
}




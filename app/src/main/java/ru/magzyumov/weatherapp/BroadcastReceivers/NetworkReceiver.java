package ru.magzyumov.weatherapp.BroadcastReceivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.MainActivity;
import ru.magzyumov.weatherapp.R;

public class NetworkReceiver extends BroadcastReceiver implements Constants {
    private MainActivity mainActivity;
    private int messageId = 1;
    private SharedPreferences sharedPreferences;

    public NetworkReceiver(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.sharedPreferences = mainActivity.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message;

        try
        {
            if (isOnline(context)) {
                mainActivity.showAlarm(true);
                message = mainActivity.getString(R.string.okInternet);
            } else {
                mainActivity.showAlarm(false);
                message = mainActivity.getString(R.string.noInternet);
            }

            if(sharedPreferences.getBoolean(NOTICE, true)) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "network")
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle(mainActivity.getString(R.string.internetConnection))
                        .setContentText(message);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(messageId, builder.build());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}


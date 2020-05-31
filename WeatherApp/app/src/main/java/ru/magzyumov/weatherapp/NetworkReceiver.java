package ru.magzyumov.weatherapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NetworkReceiver extends BroadcastReceiver {

    private boolean firstConnection;
    private boolean isOnline;
    private MainActivity mainActivity;
    private int messageId = 0;

    public NetworkReceiver(MainActivity mainActivity){
        this.mainActivity = mainActivity;
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

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "network")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mainActivity.getString(R.string.internetConnection))
                        .setContentText(message);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(messageId++, builder.build());

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


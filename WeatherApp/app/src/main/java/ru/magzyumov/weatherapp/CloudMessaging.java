package ru.magzyumov.weatherapp;


import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CloudMessaging extends FirebaseMessagingService implements Constants {
    private int messageId = 0;

    public CloudMessaging() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title;
        String text;

        try{

            title = remoteMessage.getNotification().getTitle();
            text = remoteMessage.getNotification().getBody();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "pushMessage")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(text);
            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.e("PushMessage", "Token " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }
}

package ru.magzyumov.weatherapp.CloudMessaging;


import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;


public class CloudMessaging extends FirebaseMessagingService implements Constants {
    private int messageId = 0;
    private DatabaseReference firebaseDB;

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
                    .setSmallIcon(R.drawable.ic_app)
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

    }
}


package ru.magzyumov.weatherapp.CloudMessaging;


import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.PhoneClass;
import ru.magzyumov.weatherapp.R;

import static org.apache.commons.lang3.StringUtils.capitalize;

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
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token){
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.getDefault());
        Date date = new Date();

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String timeStamp = dateFormat.format(date);
        String osVersion = Build.VERSION.RELEASE;
        String apiLevel = Build.VERSION.SDK_INT + "";
        String name = getDeviceName();

        firebaseDB = FirebaseDatabase.getInstance().getReference();
        PhoneClass newPhone = new PhoneClass(id, timeStamp, osVersion, apiLevel, name, token);
        firebaseDB.child(PHONES).child(id).child(INSTALLATION).setValue(newPhone.getInstallation());
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }
}


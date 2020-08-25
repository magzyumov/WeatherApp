package ru.magzyumov.weatherapp.CloudMessaging;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.PhoneClass;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class TokenSender implements Constants {
    private Context context;
    private DatabaseReference firebaseDB;

    public TokenSender (Context context){
        this.context = context;
    }

    public void sendRegistrationToServer(String token){
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.getDefault());
        Date date = new Date();

        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
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

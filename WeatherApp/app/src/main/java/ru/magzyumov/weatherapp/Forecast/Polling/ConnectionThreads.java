package ru.magzyumov.weatherapp.Forecast.Polling;

import android.os.Handler;
import android.os.HandlerThread;

import com.google.gson.Gson;

import ru.magzyumov.weatherapp.Constants;

public class ConnectionThreads implements Constants {
    private HandlerThread[] handlerThreads;
    private Handler[] handlers;
    private Gson gson;

    public ConnectionThreads(String... names) {
        gson = new Gson();
        handlerThreads = new HandlerThread[names.length];
        handlers = new Handler[names.length];

        for (int i = 0; i < names.length ; i++) {
            handlerThreads[i] = new HandlerThread(names[i]);
            handlerThreads[i].start();
            handlers[i] = new Handler(handlerThreads[i].getLooper());
        }
    }

    public void postTask(String name, Runnable task){
        for (int i = 0; i < handlerThreads.length ; i++) {
            if(handlerThreads[i].getName().equals(name)){
                handlers[i].post(task);
                break;
            }
        }
    }
}
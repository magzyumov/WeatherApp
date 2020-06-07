package ru.magzyumov.weatherapp;

import android.os.Handler;
import android.os.HandlerThread;

public class GeoMapThreads implements Constants {
    private HandlerThread[] handlerThreads;
    private Handler[] handlers;

    public GeoMapThreads(String... names) {
        if((handlers == null) && (handlers == null)){
            handlerThreads = new HandlerThread[names.length];
            handlers = new Handler[names.length];

            for (int i = 0; i < names.length ; i++) {
                handlerThreads[i] = new HandlerThread(names[i]);
                handlerThreads[i].start();
                handlers[i] = new Handler(handlerThreads[i].getLooper());
            }
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

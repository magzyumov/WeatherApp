package ru.magzyumov.weatherapp.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogWindow  {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private DialogInterface.OnClickListener negativeButtonListener;
    private DialogInterface.OnClickListener positiveButtonListener;
    private DialogInterface.OnClickListener neuralButtonListener;

    // Конструктор для Alert c одной кнопкой
    public AlertDialogWindow (Context context, String title, String message, String negativeButtonText){
        negativeButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(negativeButtonText, negativeButtonListener);
        alertDialog = builder.create();
    }

    // Конструктор для Alert c одной кнопкой и динамичным сообщением
    public AlertDialogWindow (Context context, String title, String negativeButtonText){
        negativeButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setCancelable(false)
                .setNegativeButton(negativeButtonText, negativeButtonListener);
        alertDialog = builder.create();
    }

    // Конструктор для Alert c двумя кнопками
    public AlertDialogWindow (Context context, String title, String message,
                              String negativeButtonText, String positiveButtonText){
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
                .setPositiveButton(positiveButtonText, positiveButtonListener);
        alertDialog = builder.create();
    }

    public AlertDialogWindow (Context context, String title, String message,
                              String negativeButtonText, String positiveButtonText,
                              String neuralButtonText){
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
                .setNeutralButton(neuralButtonText, neuralButtonListener)
                .setPositiveButton(positiveButtonText, positiveButtonListener);
        alertDialog = builder.create();
    }

    // Метод показа Alert
    public void show(){
        alertDialog.show();
    }

    // Метод показа Alert с динамичным сообщением
    public void show(String message){
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}

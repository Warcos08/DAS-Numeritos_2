package com.example.das_proyecto1;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.work.PeriodicWorkRequest;

import java.util.concurrent.TimeUnit;

public class NotifBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Este metodo se llama cuando la clase recibe un Intent
        // Cuando se recibe el Intent del AlarmManager, se crea y lanza la notificacion

        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(context, "notifCanal");
        // Creo el canal si la version de android lo requiere
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("notifCanal", "notifDiaria", NotificationManager.IMPORTANCE_DEFAULT);
            notifManager.createNotificationChannel(elCanal);
        }

        elBuilder.setSmallIcon(R.drawable.logo)
                .setContentTitle("NUMERITOS")
                .setContentText(context.getResources().getString(R.string.notif_diaria))
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);

        notifManager.notify(16, elBuilder.build());

    }
}

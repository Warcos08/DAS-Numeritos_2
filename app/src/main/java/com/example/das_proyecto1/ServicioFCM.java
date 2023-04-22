package com.example.das_proyecto1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFCM extends FirebaseMessagingService {

    public ServicioFCM() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Si el mensaje viene con datos
        }
        if (remoteMessage.getNotification() != null) {
            // Si el mensaje es una notificacion
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}

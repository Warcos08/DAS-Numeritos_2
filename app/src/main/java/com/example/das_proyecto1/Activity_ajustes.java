package com.example.das_proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Activity_ajustes extends AppCompatActivity {

    private static String idiomaAct = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Cargo la pagina en el idioma elegido
        if (savedInstanceState != null) {
            idiomaAct = savedInstanceState.getString("idioma");

            Locale nuevaloc;
            if (idiomaAct.equals("en")) {
                nuevaloc = new Locale("en");
            } else {
                nuevaloc = new Locale("es");
            }

            Locale.setDefault(nuevaloc);
            Configuration configuration = getBaseContext().getResources().getConfiguration();
            configuration.setLocale(nuevaloc);
            configuration.setLayoutDirection(nuevaloc);

            Context context = getBaseContext().createConfigurationContext(configuration);
            getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

            finish();
            startActivity(getIntent());
        }

        // Cambiar a español
        Button btn_es = (Button) findViewById(R.id.ajustes_btnES);
        btn_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idiomaAct = "es";
                Locale nuevaloc = new Locale("es");
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
            }
        });

        // Cambiar a ingles
        Button btn_en = (Button) findViewById(R.id.ajustes_btnEN);
        btn_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idiomaAct = "en";
                Locale nuevaloc = new Locale("en");
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
            }
        });

        // Abrir las preferencias
        Button btn_preferencias = (Button) findViewById(R.id.ajustes_btn_preferencias);
        btn_preferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la actividad / fragment con las preferencias
            }
        });

        // Volver a inicio
        Button btn_salir = (Button) findViewById(R.id.ajustes_btn_salir);
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mando una notificacion local de que se han guardado los cambios
                if (ContextCompat.checkSelfPermission(Activity_ajustes.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // Pido los permisos para la notificacion si no los tengo ya
                    ActivityCompat.requestPermissions(Activity_ajustes.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
                    return;
                }

                NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(Activity_ajustes.this, "notifCanal");
                // Creo el canal si la version de android lo requiere
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel elCanal = new NotificationChannel("notifCanal", "notifAjustes", NotificationManager.IMPORTANCE_DEFAULT);
                    // Caracteristicas de la notificacion
                    elBuilder.setSmallIcon(R.drawable.gold_trans)
                            //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.gold_trans))
                            .setContentTitle("NUMERITOS")
                            .setContentText(getString(R.string.ajustes_msg_notif))
                            .setVibrate(new long[]{0, 1000, 500, 1000})
                            .setAutoCancel(true);

                    notifManager.createNotificationChannel(elCanal);
                }

                notifManager.notify(11, elBuilder.build());

                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            }
        });

    }

    // Para guardar la info cuando se rote la pantalla
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Guardo el idioma del dispositivo
        savedInstanceState.putString("idioma", idiomaAct);
    }

}
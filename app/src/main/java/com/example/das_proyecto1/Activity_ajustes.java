package com.example.das_proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        // Si no se tienen permisos para notificaciones, se piden
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }

        // Miro que tema ha sido elegido
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch(prefs.getString("temaPref", null)) {
            case "Theme.Bosque":
                System.out.println("############## BOSQUE ##############");
                setTheme(R.style.Theme_Bosque);
                break;
            case "Theme.Mar":
                System.out.println("############## MAR ##############");
                setTheme(R.style.Theme_Mar);
                break;
            default:
                System.out.println("############## OTRO ##############");
                setTheme((R.style.Theme_DAS_Proyecto1));
                break;
        }

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

        getSupportActionBar().setTitle(getString(R.string.ajustes_text_ajustes));

        /*
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
                    elBuilder.setSmallIcon(R.drawable.logo)
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
        */

    }

    @Override
    public void onBackPressed() {
        // Para que la actividad no siga activa al salir de ella
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
    }

    // Para guardar la info cuando se rote la pantalla
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Guardo el idioma del dispositivo
        savedInstanceState.putString("idioma", idiomaAct);
    }

}
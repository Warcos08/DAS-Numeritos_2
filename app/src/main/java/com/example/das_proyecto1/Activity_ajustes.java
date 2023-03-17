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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Miro que tema ha sido elegido
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tema = prefs.getString("temaPref", "1");
        switch(tema) {
            case "1":
                System.out.println("############## " + tema + " ##############");
                setTheme(R.style.tema_claro);
                break;
            case "2":
                System.out.println("############## " + tema + " ##############");
                setTheme(R.style.tema_claro);
                break;
            case "3":
                System.out.println("############## " + tema + " ##############");
                setTheme(R.style.tema_bosque);
                break;
            case "4":
                System.out.println("############## " + tema + " ##############");
                setTheme(R.style.tema_mar);
                break;
            default:
                System.out.println("############## OTRO ##############");
                setTheme(R.style.tema_claro);
                break;
        }

        // Cargo la pagina en el idioma elegido
        Locale nuevaloc;
        if (prefs.getString("idiomaPref", "1").equals("2")) {
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Si no se tienen permisos para notificaciones, se piden
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }

        // Poner un texto personalizado en la ActionBar
        getSupportActionBar().setTitle(getString(R.string.ajustes_text_ajustes));

    }

    @Override
    public void onBackPressed() {
        // Para que la actividad no siga activa al salir de ella
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

}
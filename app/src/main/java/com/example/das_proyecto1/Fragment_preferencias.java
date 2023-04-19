package com.example.das_proyecto1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Fragment_preferencias extends PreferenceFragmentCompat
                                    implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        // Layout con la pantalla de preferencias
        addPreferencesFromResource(R.xml.preferencias);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Listener para cuando cambia algo de las preferencias

        // Filtro que preferencia ha cambiado
        switch(s) {
            case "temaPref":
                // Reinicio la actividad para aplicar el tema
                getActivity().finish();
                startActivity(getActivity().getIntent());
                break;

            case "idiomaPref":
                System.out.println("############## IDIOMA ##############");
                // Cambio el idioma de la app
                Locale nuevaloc = new Locale(sharedPreferences.getString(s, null));
                Locale.setDefault(nuevaloc);
                Configuration configuration = getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getActivity().createConfigurationContext(configuration);
                getActivity().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                // Vuelvo a cargar la actividad para aplicar el idioma
                getActivity().finish();
                startActivity(getActivity().getIntent());
                break;

            case "notifPref":
                if (sharedPreferences.getBoolean("notifPref", false)) {
                    System.out.println("NOTIFICACIONES ACTIVADAS");

                    // Al activar las notificaciones diarias, configuro el AlarmManager de la notificacion diaria
                    // Creo el Intent al BroadcastReceiver, el pendingIntent y el AlarmManager
                    Intent intent = new Intent(getContext(), NotifBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                    // Obtengo el momento en el que se activa la funcion y el tiempo del intervalo
                    long tInicio = System.currentTimeMillis();
                    long tIntervalo = TimeUnit.DAYS.toMillis(1);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, tInicio, tIntervalo, pendingIntent);

                } else {
                    System.out.println("NOTIFICACIONES DESACTIVADAS");

                    // En caso de que se desactiven, cancelo la notificacion diaria
                    // Creo el AlarmManager exactamente igual y lo cancelo
                    Intent intent = new Intent(getContext(), NotifBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                    alarmManager.cancel(pendingIntent);

                }

                break;

            default:
                break;

        }
    }

    // Metodos para gestionar la vida del listener
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

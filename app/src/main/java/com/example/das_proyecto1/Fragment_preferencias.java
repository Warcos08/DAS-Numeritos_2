package com.example.das_proyecto1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

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
                System.out.println("###################### NOTIFS");

                break;

            default:
                break;

        }
    }

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

package com.example.das_proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class activity_ajustes extends AppCompatActivity {

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
        Button btn_guardar = (Button) findViewById(R.id.ajustes_btn_salir);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
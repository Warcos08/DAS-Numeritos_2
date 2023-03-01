package com.example.das_proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class actividad_ajustes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_ajustes);

        // Cambiar a español
        Button btn_es = (Button) findViewById(R.id.ajustes_btnES);
        btn_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


}
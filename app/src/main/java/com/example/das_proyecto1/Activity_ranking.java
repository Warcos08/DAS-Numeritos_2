package com.example.das_proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Locale;

public class Activity_ranking extends AppCompatActivity {

    // Aquello que vamos a mostrar en el ListView
    int[] imagenes = {R.drawable.gold_trans, R.drawable.silver_trans, R.drawable.bronze_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans};
    String[] users = new String[10];
    int[] ptos = new int[10];
    ListView listView;  // El ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Miro que tema ha sido elegido
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tema = prefs.getString("temaPref", "1");
        switch(tema) {
            case "1":
                System.out.println("##############" + tema + " ##############");
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
        setContentView(R.layout.activity_ranking);

        // Poner un texto personalizado en la ActionBar
        getSupportActionBar().setTitle(getString(R.string.ranking_titulo));

        // Obtener lo que necesitamos mostrar de la BD
        BD gestorBD = new BD(Activity_ranking.this, "miBD", null, 1);
        SQLiteDatabase bd = gestorBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * " +
                                        "FROM Puntuaciones " +
                                        "ORDER BY Puntuacion DESC " +
                                        "LIMIT 10", null);

        // Meto la info en las variables
        int i = 0;
        while(cursor.moveToNext()) {
            users[i] = cursor.getString(1);
            System.out.println(users[i]);
            ptos[i] = cursor.getInt(2);
            System.out.println(ptos[i]);
            i++;
        }

        // En caso de que haya menos info de la que se quiere mostrar, se rellena
        while (i < 5) {
            users[i] = "-";
            ptos[i] = 0;
            i++;
        }

        listView = (ListView) findViewById(R.id.ranking_list);
        listView.setEnabled(false);
        // Le paso al adapter lo que necesita
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), imagenes, users, ptos);
        listView.setAdapter(customAdapter);
    }

    @Override
    public void onBackPressed() {
        // Para que la actividad no siga activa al salir de ella
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }
}
package com.example.das_proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Locale;

public class Activity_ranking extends AppCompatActivity {

    // Aquello que vamos a mostrar en el ListView
    int[] imgs_rank = {R.drawable.gold_trans, R.drawable.silver_trans, R.drawable.bronze_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans};
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
                System.out.println("############## " + tema + " ##############");
                // Cambiar el color de la ActionBar (para el modo nocturno)
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFBB86FC")));
                setTheme(R.style.tema_claro);
                break;
            case "2":
                System.out.println("############## " + tema + " ##############");
                setTheme(R.style.tema_bosque);
                break;
            case "3":
                System.out.println("############## " + tema + " ##############");
                setTheme(R.style.tema_mar);
                break;
            default:
                System.out.println("############## OTRO ##############");
                // Cambiar el color de la ActionBar (para el modo nocturno)
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFBB86FC")));
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

        Data datosSelect = new Data.Builder()
                .putString("peticion", "selectPuntuaciones")
                .putInt("opcion", 1)
                .build();

        // Crear la peticion a la BD
        OneTimeWorkRequest selectRanking = new OneTimeWorkRequest.Builder(ConexionBD.class)
                .setInputData(datosSelect)
                .build();

        // Observer que comprueba que la peticion se realice
        WorkManager.getInstance(Activity_ranking.this).getWorkInfoByIdLiveData(selectRanking.getId()).observe(Activity_ranking.this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    Data output = workInfo.getOutputData();
                    if (!output.getString("resultado").equals("Top 1")) {
                        JSONParser parser = new JSONParser();
                        try {
                            // Obtengo la información de las partidas
                            JSONArray jsonResultado = (JSONArray) parser.parse(output.getString("resultado"));

                            Integer i = 0;
                            while (i < jsonResultado.size()) {
                                JSONObject row = (JSONObject) jsonResultado.get(i);
                                // Por defecto todos los datos son Strings, hay que convertirlos al tipo adecuado
                                users[i] = (String) row.get("Username");
                                String puntuacionS = (String) row.get("Puntuacion");
                                ptos[i] = Integer.parseInt(puntuacionS);
                                i++;
                            }

                            // En caso de que haya menos info de la que se quiere mostrar, se rellena
                            while (i < 10) {
                                users[i] = "-";
                                ptos[i] = 0;
                                i++;
                            }

                            listView = (ListView) findViewById(R.id.ranking_list);
                            listView.setEnabled(false);
                            // Le paso al adapter lo que necesita
                            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), imgs_rank, users, ptos);
                            listView.setAdapter(customAdapter);

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        });
        WorkManager.getInstance(Activity_ranking.this).enqueue(selectRanking);

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
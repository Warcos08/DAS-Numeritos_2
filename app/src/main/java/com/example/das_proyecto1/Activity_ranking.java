package com.example.das_proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class Activity_ranking extends AppCompatActivity {

    // Aquello que vamos a mostrar en el ListView
    int[] imagenes = {R.drawable.gold_trans, R.drawable.silver_trans, R.drawable.bronze_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans, R.drawable.blank_trans};
    String[] users = new String[10];
    int[] ptos = new int[10];

    // El ListView
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

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
        finish();
    }
}
package com.example.das_proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class Activity_ranking extends AppCompatActivity {

    // Aquello que vamos a mostrar en el ListView
    int[] imagenes = {R.drawable.gold_trans, R.drawable.silver_trans, R.drawable.bronze_trans, R.drawable.blank_trans, R.drawable.blank_trans};
    String[] users;
    int[] ptos;

    // El ListView
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Obtener lo que necesitamos mostrar de la BD
        BD gestorBD = new BD(Activity_ranking.this, "miBD", null, 1);
        SQLiteDatabase bd = gestorBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT Username, Puntuacion " +
                                        "FROM Puntuaciones " +
                                        "ORDER BY Puntuaciones DESC " +
                                        "LIMIT 5", null);

        // Meto la info en las variables
        int i = 0;
        while(cursor.moveToNext()) {
            users[i] = cursor.getString(0);
            System.out.println(users[i]);
            ptos[i] = cursor.getInt(1);
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
        // Le paso al adapter lo que necesita
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), imagenes, users, ptos);
        listView.setAdapter(customAdapter);
    }
}
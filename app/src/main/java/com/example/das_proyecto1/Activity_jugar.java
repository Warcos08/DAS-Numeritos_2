package com.example.das_proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Activity_jugar extends AppCompatActivity {

    private static String idiomaAct = "";
    private static Juego miJuego;
    private static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);
        System.out.println("###################### onCREATE ######################");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
        System.out.println("###################### USER: " + username);


        if (savedInstanceState != null) {

            // Num
            System.out.println("###################### NUM: " + miJuego.getNum());

            // Cifra
            System.out.println("###################### CIFRA: " + miJuego.getCifra());

            // Idioma
            idiomaAct = savedInstanceState.getString("idioma");
            System.out.println("###################### IDIOMA: " + idiomaAct);

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

            // Puntuacion
            System.out.println("###################### PTOS: " + miJuego.getPuntuacion());
        }

        if (miJuego == null) {
            miJuego = new Juego();
        }

        // Boton de responder
        Button btn_responder = (Button) findViewById(R.id.jugar_respuesta);
        btn_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tomo el valor introducido en el input
                EditText input = (EditText) findViewById(R.id.jugar_input);
                String respuesta = input.getText().toString();

                // Mensaje por si no se introduce una respuesta
                String msg_inputVacio = getString(R.string.jugar_msg_input_vacio);

                if (respuesta.equals("")) {
                    // Si la respuesta esta vacía
                    Toast.makeText(Activity_jugar.this, msg_inputVacio, Toast.LENGTH_SHORT).show();
                } else if (!miJuego.comprobarRespuesta(Integer.parseInt(respuesta))) {
                    // Si la respuesta es incorrecta

                    // Guardar la puntuacion en la BD
                    BD gestorBD = new BD(Activity_jugar.this, "miBD", null, 1);
                    SQLiteDatabase bd = gestorBD.getReadableDatabase();

                    ContentValues datos = new ContentValues();
                    datos.put("Username", username);
                    datos.put("Puntuacion", miJuego.getPuntuacion());

                    bd.insert("Puntuaciones", null, datos);


                    // Notificaciones si ha alcanzado el ranking
                    Cursor c = bd.rawQuery("SELECT Puntuacion FROM Puntuaciones " +
                                                "WHERE Puntuacion > " + "'" + miJuego.getPuntuacion() + "'",null);

                    NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(Activity_jugar.this, "notifCanal");
                    // Creo el canal si la version de android lo requiere
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel elCanal = new NotificationChannel("notifCanal", "notifRanking", NotificationManager.IMPORTANCE_DEFAULT);
                        notifManager.createNotificationChannel(elCanal);
                    }

                    elBuilder.setSmallIcon(R.drawable.logo)
                            .setContentTitle("NUMERITOS")
                            .setVibrate(new long[]{0, 1000, 500, 1000})
                            .setAutoCancel(true);

                    switch (c.getCount()) {
                        case 0:
                            // Notificacion de que estas primero
                            elBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.gold_trans))
                                    .setContentText(getString(R.string.jugar_msg_rank1));
                            notifManager.notify(12, elBuilder.build());
                            break;
                        case 1:
                            // Notificacion de que estas segundo
                            elBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.silver_trans))
                                    .setContentText(getString(R.string.jugar_msg_rank2));
                            notifManager.notify(12, elBuilder.build());
                            break;
                        case 2:
                            // Notificacion de que estas tercero
                            elBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bronze_trans))
                                    .setContentText(getString(R.string.jugar_msg_rank3));
                            notifManager.notify(12, elBuilder.build());
                            break;
                        default:
                            break;
                    }


                    // Dialogo de "Has perdido"
                    Bundle args = new Bundle();
                    args.putInt("ptos", miJuego.getPuntuacion());
                    miJuego = null;     // Termino la partida
                    DialogFragment dialogo_derrota = new Dialogo_derrota();
                    dialogo_derrota.setArguments(args);
                    dialogo_derrota.show(getSupportFragmentManager(), "dialogo_derrota");

                } else {
                    // Si la respuesta es correcta

                    // Sumar 1 a la puntuacion
                    TextView txt_ptos = (TextView) findViewById(R.id.jugar_ptos);
                    miJuego.sumarPunto();
                    txt_ptos.setText(Integer.toString(miJuego.getPuntuacion()));

                    // Borrar el input
                    input.setText("");

                    // Generar otro numero aleatorio y actualizo la vista
                    miJuego.setCifraRandom();
                    TextView txt_cifra = (TextView) findViewById(R.id.jugar_numeros);
                    txt_cifra.setText(Integer.toString(miJuego.getCifra()));
                }

            }
        });


        // Boton de como jugar
        Button btn_instrucciones = (Button) findViewById(R.id.jugar_instrucciones);
        btn_instrucciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogo_instrucciones = new Dialogo_instrucciones();
                dialogo_instrucciones.show(getSupportFragmentManager(), "dialogo_instrucciones");
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("###################### onSTART ######################");

        // Al volver de compartir la puntuacion termino la actividad
        if (miJuego == null) {
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        } else {
            // Actualizo los elementos de la vista
            TextView txt_ptos = (TextView) findViewById(R.id.jugar_ptos);
            txt_ptos.setText(Integer.toString(miJuego.getPuntuacion()));

            TextView txt_num = (TextView) findViewById(R.id.jugar_numeros);
            txt_num.setText(Integer.toString(miJuego.getCifra()));
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        idiomaAct = getResources().getConfiguration().getLocales().get(0).getLanguage();
        savedInstanceState.putString("idioma", idiomaAct);
    }
}
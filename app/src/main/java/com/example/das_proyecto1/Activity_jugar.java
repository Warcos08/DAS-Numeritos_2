package com.example.das_proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.res.Configuration;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);
        System.out.println("###################### onCREATE ######################");


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

                    // Guardar la puntuacion en la BD / fichero de texto

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

        // Actualizo los elementos de la vista
        TextView txt_ptos = (TextView) findViewById(R.id.jugar_ptos);
        txt_ptos.setText(Integer.toString(miJuego.getPuntuacion()));

        TextView txt_num = (TextView) findViewById(R.id.jugar_numeros);
        txt_num.setText(Integer.toString(miJuego.getCifra()));
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Guardo el num, la puntuacion y el idioma
        /*
        savedInstanceState.putInt("num", num);
        savedInstanceState.putInt("ptos", ptos);
        savedInstanceState.putInt("cifra", cifra);
        */

        idiomaAct = getResources().getConfiguration().getLocales().get(0).getLanguage();
        savedInstanceState.putString("idioma", idiomaAct);
    }
}
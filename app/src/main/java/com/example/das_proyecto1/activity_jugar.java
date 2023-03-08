package com.example.das_proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class activity_jugar extends AppCompatActivity {

    private static String idiomaAct = "";
    private static int num = -1;
    private static int ptos = 0;
    private static int cifra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        if (savedInstanceState != null) {

            // Num
            num = savedInstanceState.getInt("num");
            System.out.println("###################### NUM: " + num);

            // Cifra
            cifra = savedInstanceState.getInt("cifra");
            System.out.println("###################### CIFRA: " + cifra);

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
            TextView txt_ptos = (TextView) findViewById(R.id.jugar_ptos);
            ptos = savedInstanceState.getInt("ptos");
            txt_ptos.setText(Integer.toString(ptos));
            System.out.println("###################### PTOS: " + ptos);
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
                    Toast.makeText(activity_jugar.this, msg_inputVacio, Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(respuesta) != num) {
                    // Si la respuesta es incorrecta

                    // Guardar la puntuacion en la BD / fichero de texto

                    // Dialogo de "Has perdido"
                    Bundle args = new Bundle();
                    args.putInt("ptos", ptos);
                    DialogFragment dialogo_derrota = new dialogo_derrota();
                    dialogo_derrota.setArguments(args);
                    dialogo_derrota.show(getSupportFragmentManager(), "dialogo_derrota");


                } else {
                    // Si la respuesta es correcta

                    // Sumar 1 a la puntuacion
                    TextView txt_ptos = (TextView) findViewById(R.id.jugar_ptos);
                    ptos += 1;
                    txt_ptos.setText(Integer.toString(ptos));

                    // Borrar el input
                    input.setText("");

                    // Generar otro numero aleatorio
                    generarNumero();
                }

            }
        });


        // Boton de como jugar
        Button btn_instrucciones = (Button) findViewById(R.id.jugar_instrucciones);
        btn_instrucciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogo_instrucciones = new dialogo_instrucciones();
                dialogo_instrucciones.show(getSupportFragmentManager(), "dialogo_instrucciones");
            }
        });

        // Genero el primero numero aleatorio
        if (num == -1) {
            generarNumero();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Actualizo los elementos de la vista
        TextView txt_ptos = (TextView) findViewById(R.id.jugar_ptos);
        txt_ptos.setText(Integer.toString(ptos));

        TextView txt_num = (TextView) findViewById(R.id.jugar_numeros);
        txt_num.setText(Integer.toString(cifra));
    }

    public void generarNumero() {
        // Genero una cifra aleatoria
        Random random = new Random();
        cifra = random.nextInt(10);

        System.out.println("######################### " + cifra);

        // La muestro en el textView
        TextView txt_num = (TextView) findViewById(R.id.jugar_numeros);
        txt_num.setText(Integer.toString(cifra));
        // Espero un segundo
        /*
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        */
        // Borro el contenido del textView
        //txt_num.setText("");

        // Actualizo la variable num
        if (num == -1) {
            num = 0;
        }
        num = num * 10 + cifra;
    }

    public int getScore() {
        return ptos;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Guardo el num, la puntuacion y el idioma
        savedInstanceState.putInt("num", num);
        savedInstanceState.putInt("ptos", ptos);
        savedInstanceState.putInt("cifra", cifra);

        idiomaAct = getResources().getConfiguration().getLocales().get(0).getLanguage();
        savedInstanceState.putString("idioma", idiomaAct);
    }
}
package com.example.das_proyecto1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Activity_inicio extends AppCompatActivity {

    // Codigos para gestionar los intents de las diferentes actividades
    private static int resultCodeAjustes = 3;
    private static String idiomaAct = "";


    // Metodos del ciclo de vida de la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Cargo la pagina en el idioma elegido
        if (savedInstanceState != null) {
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
        }
    }


    // Metodos onClick

    public void onClickJugar(View v) {
        // El usuario se puede definir como parte de las preferencias
        // En caso de no estar logueado, deberia saltar un dialogo que de opcion de hacer login o register
        // notificacion cuando te logueas/registras con exito??

        // Cojo el nombre del usuario logueado
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.contains("username")) {
            // Ir a la pantalla de juego
            System.out.println("CON USERNAME");
        } else {
            // Opcion de loguearse
            System.out.println("SIN USERNAME");
            showDialogoLogin();
        }

    }

    public void onClickRanking(View v) {
        Intent intent = new Intent(Activity_inicio.this, Activity_ranking.class);
        startActivityIntent.launch(intent);
    }

    public void onClickAjustes(View v) {
        Intent intent = new Intent(Activity_inicio.this, Activity_ajustes.class);
        startActivityIntent.launch(intent);
    }

    public void onClickSalir(View v) {
        //deleteDatabase("miBD");
        DialogFragment dialogo_salir = new Dialogo_salir();
        dialogo_salir.show(getSupportFragmentManager(), "dialogo_salir");
    }


    // Funcion que crea el dialogo de login
    void showDialogoLogin() {
        // Creo el dialogo de login con el layout
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogo_login);
        dialog.setCancelable(true);

        // Funcion del boton de login
        Button btn_login = (Button) dialog.findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtengo el nombre de usuario introducido
                EditText input_user = dialog.findViewById(R.id.login_input_username);
                String user = input_user.getText().toString();
                // Obtengo la contraseña
                EditText input_password = dialog.findViewById(R.id.login_input_password);
                String password = input_password.getText().toString();

                String msg1 = getString(R.string.login_msg_campos_vacios);
                String msg2 = getString(R.string.login_msg_error);
                String msg3 = getString(R.string.login_msg_log);

                if (user.equals("") || password.equals("")) {
                    // En caso de que no se haya rellenado el formulario mando un mensaje
                    Toast.makeText(Activity_inicio.this, msg1, Toast.LENGTH_SHORT).show();

                } else {
                    // Obtengo la BD
                    BD gestorBD = new BD(Activity_inicio.this, "miBD", null, 1);
                    SQLiteDatabase bd = gestorBD.getWritableDatabase();

                    // Compruebo que el usuario se encuentre en la BD
                    Cursor c = bd.rawQuery("SELECT * FROM Usuarios " +
                            "WHERE Username = '" + user + "' " + "AND Password = '" + password + "'", null);

                    // Si hay next es que existe ese usuario con esa contraseña
                    if (c.moveToNext()) {
                        Toast.makeText(Activity_inicio.this, msg3, Toast.LENGTH_SHORT).show();
                        // Obtengo el username
                        String username = c.getString(0);
                        System.out.println("############### " + username);
                        System.out.println("############### " + user);
                        // Paso a la actividad jugar
                        Intent intent = new Intent(Activity_inicio.this, Activity_jugar.class);
                        intent.putExtra("username", username);
                        startActivityIntent.launch(intent);

                    } else {
                        Toast.makeText(Activity_inicio.this, msg2, Toast.LENGTH_SHORT).show();
                    }

                }
            }


        });

        // Funcion del texto de registrarse
        TextView text_registro = (TextView) dialog.findViewById(R.id.login_registro);
        text_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierro el dialogo de login y abro el de registro
                dialog.dismiss();
                showDialogoRegistro();
            }
        });

        dialog.show();
    }

    // Funcion que crea el dialogo de registrarse
    public void showDialogoRegistro() {
        // Creo el dialogo de login con el layout
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogo_register);
        dialog.setCancelable(true);

        // Funcion del boton de registro
        Button btn_registro = (Button) dialog.findViewById(R.id.btn_registro);
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Según el idioma seteo los mensajes que van a aparecer
                String msg_campos_vacios = getString(R.string.register_msg_campos_vacios);
                String msg_contraseñas = getString(R.string.register_msg_passwords);
                String msg_registro = getString(R.string.register_msg_registro);
                String msg_usuario = getString(R.string.login_msg_usuario);


                // Obtengo los datos introducidos
                TextView txt_nombre = (TextView) dialog.findViewById(R.id.register_nombre);
                String nombre = txt_nombre.getText().toString();
                TextView txt_apellidos = (TextView) dialog.findViewById(R.id.register_apellidos);
                String apellidos = txt_apellidos.getText().toString();
                TextView txt_user = (TextView) dialog.findViewById(R.id.register_username);
                String username = txt_user.getText().toString();
                TextView txt_pwd = (TextView) dialog.findViewById(R.id.register_password);
                String pwd = txt_pwd.getText().toString();
                TextView txt_pwd2 = (TextView) dialog.findViewById(R.id.register_password2);
                String pwd2 = txt_pwd2.getText().toString();

                // Obtengo la BD
                BD gestorBD = new BD(Activity_inicio.this, "miBD", null, 1);
                SQLiteDatabase bd = gestorBD.getWritableDatabase();

                // Miro en la BD si ya existe un username con ese nombre
                Cursor c = bd.rawQuery("SELECT Username FROM Usuarios WHERE Username = " + "'" + username + "'", null);

                if (nombre.equals("") || apellidos.equals("") || username.equals("") || pwd.equals("") || pwd2.equals("")) {
                    // Alguno de los campos no ha sido rellenado
                    Toast.makeText(Activity_inicio.this, msg_campos_vacios, Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(pwd2)) {
                    // Las dos contraseñas no coinciden
                    Toast.makeText(Activity_inicio.this, msg_contraseñas, Toast.LENGTH_SHORT).show();
                } else if (c.moveToNext()) {
                    // Existe un usuario con ese nombre registrado
                    Toast.makeText(Activity_inicio.this, msg_usuario, Toast.LENGTH_SHORT).show();
                } else {
                    // Todos los campos son correctos, realizo el registro

                    // Introduzco los datos
                    ContentValues datos = new ContentValues();
                    datos.put("Username", username);
                    datos.put("Nombre", nombre);
                    datos.put("Apellidos", apellidos);
                    datos.put("Password", pwd);

                    bd.insert("Usuarios", null, datos);

                    Toast.makeText(Activity_inicio.this, msg_registro, Toast.LENGTH_SHORT).show();

                    // Inicio el juego
                    Intent intent = new Intent(Activity_inicio.this, Activity_jugar.class);
                    intent.putExtra("username", username);
                    startActivityIntent.launch(intent);

                }

            }
        });

        dialog.show();
    }


    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Locale locale = getResources().getConfiguration().getLocales().get(0);
            idiomaAct = locale.getLanguage();

            finish();
            startActivity(getIntent());

        }
    });


    // Para guardar la info cuando se rote la pantalla
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Guardo el idioma del dispositivo
        savedInstanceState.putString("idioma", idiomaAct);
    }
}
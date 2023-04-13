package com.example.das_proyecto1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Activity_perfil extends AppCompatActivity {

    private static String username = "";
    private static byte[] img_bytes;

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
                setTheme(R.style.tema_bosque);
                break;
            case "3":
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
        setContentView(R.layout.activity_perfil);

        // Guardo el nombre del usuario logueado
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Obtengo el user que esta jugando para más tarde almacenar la puntuacion
            username = extras.getString("username");
        }
        System.out.println("###################### USER: " + username);

        // Hago que el input con el username tenga ya el nombre del usuario logueado
        EditText input_username = (EditText) findViewById(R.id.input_username);
        input_username.setText(username);

        // Cargo la foto del usuario almacenada en la BD
        BD gestorBD = new BD(Activity_perfil.this, "miBD", null, 1);
        SQLiteDatabase bd = gestorBD.getWritableDatabase();

        if (img_bytes == null) {
            System.out.println("##### BYTES estan a NULL #####");
            Cursor c = bd.rawQuery("SELECT Foto FROM Usuarios WHERE Username = " + "'" + username + "'", null);
            c.moveToFirst();
            img_bytes = c.getBlob(0);
        }

        System.out.println(img_bytes);

        ImageView marco = (ImageView) findViewById(R.id.img_perfil);
        Bitmap img_perfil = Utility.getImage(img_bytes);
        if (img_perfil != null) {
            System.out.println(img_perfil);
        } else {
            System.out.println("##### LA IMAGEN ES NULL POR ALGUNA RAZON #####");
        }
        marco.setImageBitmap(img_perfil);

        // Boton de tomar foto
        Button btn_foto = (Button) findViewById(R.id.btn_foto);
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = 0;
                // Si no se tienen permisos para usar la camara, se piden
                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.CAMERA}, 20);
                } else {
                    i += 1;
                }

                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 20);
                } else {
                    i += 1;
                }

                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
                } else {
                    i += 1;
                }

                if (i == 3) {
                    Intent elIntentFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureLauncher.launch(elIntentFoto);
                }


                /**
                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.CAMERA}, 20);
                }

                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.CAMERA}, 20);
                }
                **/

            }
        });

        // Boton de elegir foto
        Button btn_galeria = (Button) findViewById(R.id.btn_galeria);
        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = 0;
                // Si no se tienen permisos para acceder a la galeria se piden
                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 20);
                } else {
                    i += 1;
                }

                if (ContextCompat.checkSelfPermission(Activity_perfil.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_perfil.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
                } else {
                    i += 1;
                }

                if (i == 2) {
                    pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                }

            }
        });

        // Boton de guardar los cambios y salir
        Button btn_salir = (Button) findViewById(R.id.btn_guardar_perfil);
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg_usuario = getString(R.string.login_msg_usuario);

                // Obtengo los campos que han cambiado
                EditText input_username = (EditText) findViewById(R.id.input_username);
                String nuevo_user = String.valueOf(input_username.getText());

                /** Guardo los cambios en la BD **/
                // Obtengo la BD
                BD gestorBD = new BD(Activity_perfil.this, "miBD", null, 1);
                SQLiteDatabase bd = gestorBD.getWritableDatabase();

                // Miro si se ha cambiado el nombre de usuario
                if (!username.equals(nuevo_user)) {
                    Cursor c = bd.rawQuery("SELECT Username FROM Usuarios WHERE Username = " + "'" + nuevo_user + "'", null);
                    if (c.moveToNext()) {
                        // Si ya existe un usuario con ese nombre
                        Toast.makeText(Activity_perfil.this, msg_usuario, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Si el nombre es valido, lo actualizo junto a la foto
                    ContentValues cv = new ContentValues();
                    cv.put("Username", nuevo_user);
                    cv.put("Foto", img_bytes);
                    bd.update("Usuarios", cv, "Username='" + username + "'", null);

                } else {
                    // Si solo se ha cambiado la foto, la actualizo
                    ContentValues cv = new ContentValues();
                    cv.put("Foto", img_bytes);
                    bd.update("Usuarios", cv, "Username='" + username + "'", null);
                }

                Intent intent = new Intent();
                intent.putExtra("username", nuevo_user);
                setResult(33, intent);
                finish();
            }
        });

    }

    // Recoger el intent de la foto tomada por la camara
    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                Bundle bundle = result.getData().getExtras();
                ImageView img_perfil = findViewById(R.id.img_perfil);
                Bitmap miniatura = (Bitmap) bundle.get("data");
                img_bytes = Utility.getBitmapAsByteArray(miniatura);
                System.out.println(img_bytes);
                img_perfil.setImageBitmap(miniatura);
            } else {
                Log.d("TakenPicture", "No photo taken");
            }
    });

    // Recoger el intent de elegir una foto de la galeria
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                Bitmap bmap = null;
                try {
                    bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                img_bytes = Utility.getBitmapAsByteArray(bmap);
                System.out.println(img_bytes);
                ImageView marco = findViewById(R.id.img_perfil);
                marco.setImageURI(uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
    });

}
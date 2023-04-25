package com.example.das_proyecto1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

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
import android.util.Base64;
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
    //private static byte[] img_bytes;
    static String foto;

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

        // Por defecto cargo la por defecto
        ImageView img = (ImageView) findViewById(R.id.img_perfil);
        img.setImageResource(R.drawable.perfil);

        Data datosSelect = new Data.Builder()
                .putString("peticion", "selectUsuario")
                .putString("usuario", username)
                .build();

        // Crear la peticion a la BD
        OneTimeWorkRequest selectUser = new OneTimeWorkRequest.Builder(ConexionBD.class)
                .setInputData(datosSelect)
                .build();

        // Observer que comprueba que la peticion se realice
        WorkManager.getInstance(Activity_perfil.this).getWorkInfoByIdLiveData(selectUser.getId()).observe(Activity_perfil.this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    // En este caso no nos importa el output de la peticion, solo que se realice y que se cargue la imagen
                    Data output = workInfo.getOutputData();
                    if (!output.getString("resultado").equals("El usuario no existe") && foto != null) {

                        /** Código para convertir un String en un BitMap
                         *  Pregunta de StackOverflow: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
                         *  Autor de la respuesta: https://stackoverflow.com/users/1191766/sachin10
                         */
                        byte [] encodeByte = Base64.decode(foto, Base64.DEFAULT);
                        Bitmap bmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                        ImageView img = (ImageView) findViewById(R.id.img_perfil);
                        img.setImageBitmap(bmap);

                    } else {
                        // En caso de que se de un fallo cargo la imagen por defecto
                        ImageView img = (ImageView) findViewById(R.id.img_perfil);
                        img.setImageResource(R.drawable.perfil);
                    }
                }
            }
        });
        WorkManager.getInstance(Activity_perfil.this).enqueue(selectUser);

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

                // Miro si se ha cambiado el nombre de usuario
                if (!username.equals(nuevo_user)) {
                    // Compruebo si el nombre nuevo ya existe en la BD
                    Data datos = new Data.Builder()
                            .putString("peticion", "selectUsuario")
                            .putString("usuario", nuevo_user)
                            .build();

                    // Crear la peticion a la BD
                    OneTimeWorkRequest selectUser = new OneTimeWorkRequest.Builder(ConexionBD.class)
                            .setInputData(datos)
                            .build();

                    // Observer que comprueba que la peticion se realice
                    WorkManager.getInstance(Activity_perfil.this).getWorkInfoByIdLiveData(selectUser.getId()).observe(Activity_perfil.this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                Data output = workInfo.getOutputData();
                                if (!output.getString("resultado").equals("El usuario no existe")) {
                                    // El nombre de usuario elegido existe
                                    Toast.makeText(Activity_perfil.this, msg_usuario, Toast.LENGTH_SHORT).show();
                                } else {
                                    // El nombre de usuario elegido esta disponible, actualizo su valor
                                    Data datos = new Data.Builder()
                                            .putString("peticion", "updateUsers")
                                            .putString("userViejo", username)
                                            .putString("userNuevo", nuevo_user)
                                            .build();

                                    // Crear la peticion a la BD
                                    OneTimeWorkRequest updateUsers = new OneTimeWorkRequest.Builder(ConexionBD.class)
                                            .setInputData(datos)
                                            .build();

                                    // Observer que comprueba que la peticion se realice
                                    WorkManager.getInstance(Activity_perfil.this).getWorkInfoByIdLiveData(updateUsers.getId()).observe(Activity_perfil.this, new Observer<WorkInfo>() {
                                        @Override
                                        public void onChanged(WorkInfo workInfo) {
                                            if (workInfo != null && workInfo.getState().isFinished()) {
                                                Data output = workInfo.getOutputData();
                                                if (output.getBoolean("resultado", true)) {
                                                    // En caso de que se realice la insercion, cambio de actividad
                                                    Intent intent = new Intent();
                                                    intent.putExtra("username", nuevo_user);
                                                    setResult(33, intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    });
                                    WorkManager.getInstance(Activity_perfil.this).enqueue(updateUsers);
                                }
                            }
                        }
                    });
                    WorkManager.getInstance(Activity_perfil.this).enqueue(selectUser);

                } else {
                    // Si solo se ha cambiado la foto, la actualizo directamente
                    Data datos = new Data.Builder()
                            .putString("peticion", "updateUsers")
                            .putString("userViejo", username)
                            .putString("userNuevo", nuevo_user)
                            .build();

                    // Crear la peticion a la BD
                    OneTimeWorkRequest updateUsers = new OneTimeWorkRequest.Builder(ConexionBD.class)
                            .setInputData(datos)
                            .build();

                    // Observer que comprueba que la peticion se realice
                    WorkManager.getInstance(Activity_perfil.this).getWorkInfoByIdLiveData(updateUsers.getId()).observe(Activity_perfil.this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                Data output = workInfo.getOutputData();
                                if (output.getBoolean("resultado", true)) {
                                    // En caso de que se realice la insercion, cambio de actividad
                                    Intent intent = new Intent();
                                    intent.putExtra("username", nuevo_user);
                                    setResult(33, intent);
                                    finish();
                                }
                            }
                        }
                    });
                    WorkManager.getInstance(Activity_perfil.this).enqueue(updateUsers);
                }
            }
        });
    }

    // Recoger el intent de la foto tomada por la camara
    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                Bundle bundle = result.getData().getExtras();
                ImageView img_perfil = findViewById(R.id.img_perfil);
                Bitmap miniatura = (Bitmap) bundle.get("data");
                img_perfil.setImageBitmap(miniatura);

                /** Código para convertir un BitMap en un String
                 *  Pregunta de StackOverflow: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
                 *  Autor de la respuesta: https://stackoverflow.com/users/1191766/sachin10
                 */
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                miniatura.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
                byte[] img_bytes = Utility.getBitmapAsByteArray(miniatura);
                foto = java.util.Base64.getEncoder().encodeToString(img_bytes);

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
                    /** Código utilizado para obtener el BitMap de una imagen sacada de la galería mediante una URI
                     *  Pregunta de StackOverflow: https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
                     *  Autor de la respuesta: https://stackoverflow.com/users/986/mark-ingram
                     */
                    bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    /** Código para convertir un BitMap en un String
                     *  Pregunta de StackOverflow: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
                     *  Autor de la respuesta: https://stackoverflow.com/users/1191766/sachin10
                     */
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    bmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
                    byte[] img_bytes = Utility.getBitmapAsByteArray(bmap);
                    foto = java.util.Base64.getEncoder().encodeToString(img_bytes);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ImageView img_perfil = findViewById(R.id.img_perfil);
                img_perfil.setImageURI(uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
    });

}
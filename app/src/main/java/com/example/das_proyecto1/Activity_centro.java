package com.example.das_proyecto1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class Activity_centro extends AppCompatActivity {

    private static String username = "";

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

        setContentView(R.layout.activity_centro_nav);

        // Guardo el nombre del usuario logueado
        Bundle extras = getIntent().getExtras();
        if (extras != null && username.equals("")) {
            // Obtengo el user que esta jugando para más tarde almacenar la puntuacion
            username = extras.getString("username");
        }
        System.out.println("###################### USER: " + username);

        // Funcion boton jugar
        Button btn_jugar = (Button) findViewById(R.id.btn_jugar);
        btn_jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_centro.this, Activity_jugar.class);
                // Paso el nombre de usuario a la actividad jugar
                intent.putExtra("username", username);
                startActivityIntent.launch(intent);
            }
        });

        // Funcion boton ranking
        Button btn_ranking = (Button) findViewById(R.id.btn_ranking);
        btn_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_centro.this, Activity_ranking.class);
                startActivityIntent.launch(intent);
            }
        });

        // Funcion del boton de mapa
        Button btn_mapa = (Button) findViewById(R.id.btn_mapa);
        btn_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si no se tienen permisos para acceder a la ubicacion, se piden
                if (ContextCompat.checkSelfPermission(Activity_centro.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_centro.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 20);
                }

                if (ContextCompat.checkSelfPermission(Activity_centro.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    System.out.println("###### HA ENTRADO EN LO DE LA LOCALIZACION ######");

                    FusedLocationProviderClient provider = LocationServices.getFusedLocationProviderClient(Activity_centro.this);
                    provider.getLastLocation().addOnSuccessListener(Activity_centro.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // URI con las coordenadas del punto del mapa que quiero abrir
                                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude()));
                                // Intent para abrir el mapa
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                                finish();

                            } else {
                                System.out.println("No se ha podido encontrar la localizacion actual");
                            }
                        }
                    }).addOnFailureListener(Activity_centro.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("FAILURE");
                        }
                    });

                }
            }
        });

        // Funciones del menu desplegable
        DrawerLayout menuDesplegable = findViewById(R.id.drawer_layout);
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_perfil:
                        Intent intent_perfil = new Intent(Activity_centro.this, Activity_perfil.class);
                        intent_perfil.putExtra("username", username);
                        startActivityIntent.launch(intent_perfil);
                        break;

                    case R.id.btn_ajustes:
                        Intent intent_ajustes = new Intent(Activity_centro.this, Activity_ajustes.class);
                        startActivityIntent.launch(intent_ajustes);
                        break;

                    case R.id.btn_salir:
                        DialogFragment dialogo_salir = new Dialogo_salir();
                        dialogo_salir.show(getSupportFragmentManager(), "dialogo_salir");
                        context.deleteDatabase("miBD");
                        break;

                    default:
                        break;
                }
                menuDesplegable.closeDrawers();
                return false;
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_dialog_alert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // Si esta abierto el menu desplegable lo cierro, si no mensaje de salir
        DrawerLayout menuDesplegable = findViewById(R.id.drawer_layout);
        if (menuDesplegable.isDrawerOpen(GravityCompat.START)) {
            menuDesplegable.closeDrawer(GravityCompat.START);
        } else {
            DialogFragment dialogo_salir = new Dialogo_salir();
            dialogo_salir.show(getSupportFragmentManager(), "dialogo_salir");
        }
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // Reinicio la actividad para aplicar posibles cambios de ajustes
            System.out.println("######### REINICIO #########");
            System.out.println("######### " + Integer.toString(result.getResultCode()) + " #########");
            if (result.getResultCode() == 33) {
                username = result.getData().getStringExtra("username");
                Toast.makeText(Activity_centro.this, username, Toast.LENGTH_SHORT).show();
            }
            finish();
            startActivity(getIntent());
        }
    });
}
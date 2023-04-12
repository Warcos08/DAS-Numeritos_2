package com.example.das_proyecto1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
        if (extras != null) {
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
            System.out.println("######## REINICIO #########");
            finish();
            startActivity(getIntent());
        }
    });
}
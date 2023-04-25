package com.example.das_proyecto1;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.das_proyecto1.databinding.ActivityMapaBinding;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Activity_mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaBinding binding;
    private static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtengo el username de los extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_mapa);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Consulta a la BD para obtener las partidas del jugador
        Data datosSelect = new Data.Builder()
                .putString("peticion", "selectPuntuaciones")
                .putInt("opcion", 3)
                .putString("usuario", username)
                .build();

        // Crear la peticion a la BD
        OneTimeWorkRequest selectRanking = new OneTimeWorkRequest.Builder(ConexionBD.class)
                .setInputData(datosSelect)
                .build();

        // Observer que comprueba que la peticion se realice
        WorkManager.getInstance(Activity_mapa.this).getWorkInfoByIdLiveData(selectRanking.getId()).observe(Activity_mapa.this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    Data output = workInfo.getOutputData();
                    String resultado = output.getString("resultado");

                    System.out.println("************ " + username);
                    System.out.println(resultado);
                    if (!resultado.equals("Usuario incorrecto")) {
                        JSONParser parser = new JSONParser();
                        try {
                            // Obtengo la información de las partidas
                            JSONArray jsonResultado = (JSONArray) parser.parse(output.getString("resultado"));

                            for (int i = 0; i < jsonResultado.size(); i++) {
                                JSONObject row = (JSONObject) jsonResultado.get(i);
                                // Por defecto todos los datos son Strings, hay que convertirlos al tipo adecuado
                                String latitudS = (String) row.get("Latitud");
                                String longitudS = (String) row.get("Longitud");
                                String puntuacionS = (String) row.get("Puntuacion");

                                Double latitud = Double.parseDouble(latitudS);
                                Double longitud = Double.parseDouble(longitudS);
                                int puntuacion = (Integer) Integer.parseInt(puntuacionS);

                                // Creo el marcador y lo añado al mapa
                                LatLng partida = new LatLng(latitud, longitud);
                                mMap.addMarker(new MarkerOptions().position(partida).title("Puntuacion: " + Integer.toString(puntuacion)));
                            }

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        });
        WorkManager.getInstance(Activity_mapa.this).enqueue(selectRanking);
    }

    @Override
    public void onBackPressed() {
        // Vuelvo a la actividad centro al pulsar el boton "back"
        // Si no defino el metodo manualmente se peta
        Intent intent = new Intent(this, Activity_centro.class);
        startActivity(intent);
        finish();
    }
}
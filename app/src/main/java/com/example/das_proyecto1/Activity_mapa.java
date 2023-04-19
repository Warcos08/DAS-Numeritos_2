package com.example.das_proyecto1;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.das_proyecto1.databinding.ActivityMapaBinding;

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
        BD gestorBD = new BD(Activity_mapa.this, "miBD", null, 1);
        SQLiteDatabase bd = gestorBD.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Puntuaciones WHERE Username = '" + username + "'", null);
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                // Obtengo los datos del puntero
                double latitud = c.getDouble(3);
                double longitud = c.getDouble(4);
                int puntucion = c.getInt(2);

                System.out.println("##### " + Double.toString(latitud) + ", " + Double.toString(longitud));

                // Creo el marcador y lo añado al mapa
                LatLng partida = new LatLng(latitud, longitud);
                mMap.addMarker(new MarkerOptions().position(partida).title("Puntuacion: " + Integer.toString(puntucion)));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
    }
}
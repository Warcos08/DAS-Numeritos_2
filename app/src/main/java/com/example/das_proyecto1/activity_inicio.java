package com.example.das_proyecto1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class activity_inicio extends AppCompatActivity {

    // Codigos para gestionar los intents de las diferentes actividades
    private static int resultCodeAjustes = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void onClickSalir(View v) {
        DialogFragment dialogo_salir = new dialogo_salir();
        dialogo_salir.show(getSupportFragmentManager(), "dialogo_salir");
    }

    public void onClickAjustes(View v) {
        Intent intent = new Intent(activity_inicio.this, actividad_ajustes.class);
        startActivityIntent.launch(intent);
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // Filtro de que actividad viene el intent
            if (result.getResultCode() == resultCodeAjustes) {
                System.out.println("Has vuelto de los ajustes oleole");
            } else {
                System.out.println("Algo no ha ido como se esperaba");
            }
        }
    });


    // Para guardar la info cuando se rote la pantalla
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}
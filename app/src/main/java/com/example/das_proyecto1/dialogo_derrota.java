package com.example.das_proyecto1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.LocaleList;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class dialogo_derrota extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String titulo = getString(R.string.derrota_msg);
        // Obtengo el score que viene de la actividad
        Bundle args = getArguments();
        String ptos = Integer.toString(args.getInt("ptos"));
        String msg = getString(R.string.derrota_ptos);
        msg = msg + " " + ptos;
        String compartir = getString(R.string.derrota_compartir);
        String salir = getString(R.string.derrota_salir);

        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setNegativeButton(salir, null); // La accion de cerrar el dialogo se realiza por defecto
        builder.setPositiveButton(compartir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("####################### Aqui deberia ir algo de compartir yoquese");
            }
        });

        // Devuelvo el dialogo creado por el builder
        return builder.create();
    }

}
package com.example.das_proyecto1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialogo_salir extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String msg = getString(R.string.salir_msg);
        String si = getString(R.string.salir_msg_si);

        builder.setMessage(msg);
        builder.setNegativeButton("No", null); // La accion de cerrar el dialogo se realiza por defecto
        builder.setPositiveButton(si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });

        // Devuelvo el dialogo creado por el builder
        return builder.create();
    }

}

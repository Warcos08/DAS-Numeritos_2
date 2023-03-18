package com.example.das_proyecto1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialogo_derrota extends DialogFragment {

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        // Que hacer cuando el dialogo desaparece
        super.onDismiss(dialog);
        getActivity().finish();
    }

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
        // Boton de salir de la partida
        builder.setNegativeButton(salir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        // Boton de compartir
        builder.setPositiveButton(compartir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Obtengo el score que viene de la actividad
                Bundle args = getArguments();
                String ptos = Integer.toString(args.getInt("ptos"));

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = getString(R.string.derrota_msg_compartir) + " " + ptos + "!";
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.derrota_msg_via));
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, getString(R.string.derrota_msg_via)));
            }
        });

        // Devuelvo el dialogo creado por el builder
        return builder.create();
    }

}
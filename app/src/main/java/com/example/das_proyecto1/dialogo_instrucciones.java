package com.example.das_proyecto1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.LocaleList;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class dialogo_instrucciones extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String titulo = getString(R.string.instrucciones_msg_titulo);
        String msg = getString(R.string.instrucciones_msg);
        String si = getString(R.string.instrucciones_msg_btn);

        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setPositiveButton(si, null); // La accion de cerrar el dialogo se realiza por defecto

        // Devuelvo el dialogo creado por el builder
        return builder.create();
    }

}

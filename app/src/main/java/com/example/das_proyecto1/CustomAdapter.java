package com.example.das_proyecto1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context context;
    int[] imagenes;
    String[] users;
    int[] puntos;
    LayoutInflater inflater;

    public CustomAdapter(Context context, int[] imgs, String[] usuarios, int[] ptos) {
        this.context = context;
        this.imagenes = imgs;
        this.users = usuarios;
        this.puntos = ptos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Cojo el layout de la lista
        view = inflater.inflate(R.layout.activity_custom_list, null);
        ImageView trofeo = (ImageView) view.findViewById(R.id.ranking_img);
        TextView usuario = (TextView) view.findViewById(R.id.ranking_user);
        TextView puntuacion = (TextView) view.findViewById(R.id.ranking_ptos);

        // Asigno el valor que corresponde segun en la posicion i de la lista
        trofeo.setImageResource(imagenes[i]);
        usuario.setText(users[i]);
        puntuacion.setText(Integer.toString(puntos[i]));

        return view;
    }
}

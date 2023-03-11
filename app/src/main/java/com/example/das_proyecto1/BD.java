package com.example.das_proyecto1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {

    public BD(@Nullable Context context, @Nullable String name,
              @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creacion de la tabla que guarda los usuarios
        sqLiteDatabase.execSQL("CREATE TABLE Usuarios (" +
                "'Username' VARCHAR(255) PRIMARY KEY NOT NULL, " +
                "'Nombre' VARCHAR(255), " +
                "'Apellidos' VARCHAR(255), " +
                "'Password' VARCHAR(255)" +
                ")");

        // Creacion de la tabla que guarda las puntuaciones
        sqLiteDatabase.execSQL("CREATE TABLE Puntuaciones (" +
                "'Id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "'Username' VARCHAR(255), " +
                "'Puntuacion' INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // nose que poner aqui
    }

}

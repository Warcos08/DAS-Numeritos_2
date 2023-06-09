package com.example.das_proyecto1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.ByteArrayOutputStream;

public class Utility {

    // Funcion para convertir una imagen en un array de bytes y poder almacenarla en una base de datos
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    // Funcion para convertir un array de bytes en un bitmap y poder cargarla de la base de datos
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // Funcion para comprobar si los servicios de GooglePlay estan operativos
    public static boolean comprobarPlayServices(Context ctx){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(ctx);
        if (code == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            return false;
        }
    }
}

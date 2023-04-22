package com.example.das_proyecto1;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionBD extends Worker {

    public ConexionBD(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data datos = this.getInputData();
        String peticion = datos.getString("peticion");

        switch (peticion) {
            case "login":
                System.out.println("***** LOGIN *****");
                Data outputLogin = login();
                return Result.success(outputLogin);

            case "registro":
                System.out.println("***** REGISTRO *****");
                Data outputRegistro = registro();
                return Result.success(outputRegistro);

            case "selectUsuario":
                System.out.println("***** SELECT USUARIO *****");
                Data outputSelect = selectUsuario();
                return Result.success(outputSelect);
            default:
                return Result.failure();
        }
    }

    public Data login() {
        String url = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmerino028/WEB/selectLogin.php";
        HttpURLConnection urlConnection = null;

        Data datos = this.getInputData();
        String usuario = datos.getString("usuario");
        String contraseña = datos.getString("contraseña");

        try {
            Data output = null;

            String params = "?username=" + usuario + "&password=" + contraseña;
            URL urlFinal = new URL(url+params);
            urlConnection = (HttpURLConnection) urlFinal.openConnection();

            int status = urlConnection.getResponseCode();

            if (status == 200) {
                // La peticion ha tenido exito
                BufferedInputStream input = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String row = "";
                String resultado = "";

                // Recorro las lineas devueltas por la peticion SQL
                while ((row = reader.readLine()) != null) {
                    resultado += row;
                }

                // Devuelvo el resultado
                System.out.println(resultado);
                output = new Data.Builder().putString("resultado", resultado).build();
                return output;

            } else {
                // Devuelvo null en caso de que haya ido mal la peticion
                return output;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Data registro() {
        String url = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmerino028/WEB/insertUsers.php";
        HttpURLConnection urlConnection = null;

        Data datos = this.getInputData();
        String nombre = datos.getString("nombre");
        String apellidos = datos.getString("apellidos");
        String usuario = datos.getString("usuario");
        String contraseña = datos.getString("contraseña");
        String img = Activity_inicio.img_perfil;

        try {
            URL urlFinal = new URL(url);
            urlConnection = (HttpURLConnection) urlFinal.openConnection();
            Data output = null;
            // Peticion POST
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            // Los parametros se pasan con un JSON
            JSONObject params = new JSONObject();
            params.put("name", nombre);
            params.put("surname", apellidos);
            params.put("username", usuario);
            params.put("password", contraseña);
            params.put("photo", img);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(params.toString());
            out.close();

            int status = urlConnection.getResponseCode();
            System.out.println(status);
            if (status == 200) {
                output = new Data.Builder().putBoolean("resultado", true).build();
                return output;
            } else {
                output = new Data.Builder().putBoolean("resultado", false).build();
                return output;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Data selectUsuario() {
        String url = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmerino028/WEB/selectUsers.php";
        HttpURLConnection urlConnection = null;

        Data datos = this.getInputData();
        String usuario = datos.getString("usuario");

        try {
            Data output = null;

            String params = "?username=" + usuario;
            URL urlFinal = new URL(url+params);
            urlConnection = (HttpURLConnection) urlFinal.openConnection();

            int status = urlConnection.getResponseCode();
            System.out.println(status);
            if (status == 200) {
                // La peticion ha tenido exito
                BufferedInputStream input = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String row = "";
                String resultado = "";

                // Recorro las lineas devueltas por la peticion SQL
                while ((row = reader.readLine()) != null) {
                    resultado += row;
                }
                input.close();

                // Formo el JSON de resultado SIN la imagen
                if (!resultado.equals("El usuario no existe")) {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(resultado);

                    String username = (String) json.get("Username");
                    String nombre = (String) json.get("Nombre");
                    String apellidos = (String) json.get("Apellidos");
                    String password = (String) json.get("Password");
                    String foto = (String) json.get("Foto");

                    output = new Data.Builder()
                            .putString("usuario", username)
                            .putString("nombre", nombre)
                            .putString("apellidos", apellidos)
                            .putString("password", password)
                            .build();

                    // Pongo el valor de la imagen en la variable de la actividad Perfil
                    Activity_perfil.foto = foto;
                }

                // Devuelvo el resultado
                output = new Data.Builder().putString("resultado", resultado).build();
                return output;

            } else {
                // Devuelvo null en caso de que haya ido mal la peticion
                return output;
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
package com.example.das_proyecto1;

import android.widget.TextView;

import java.util.Random;

public class Juego {

    private static long num;
    private static int cifra;
    private static int puntuacion;

    // Constructora
    public Juego() {
        num = 0;
        cifra = this.setCifraRandom();
        puntuacion = 0;
    }

    // Metodos del juego
    public int setCifraRandom() {
        Random random = new Random();
        cifra = random.nextInt(9) + 1;

        System.out.println("######################### " + cifra);

        // Espero un segundo
        /*
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        */
        // Borro el contenido del textView
        //txt_num.setText("");

        // Actualizo num
        num = num * 10 + cifra;

        // Devuelvo cifra
        return cifra;
    }

    public boolean comprobarRespuesta(long res) {
        return res == num;
    }

    public void sumarPunto() {
        puntuacion += 1;
    }


    // Getters
    public long getNum() {
        return num;
    }

    public int getCifra() {
        return cifra;
    }

    public int getPuntuacion() {
        return puntuacion;
    }
}

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PantallaSplash extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_splash);

        // Crea un retraso para mostrar la pantalla de presentación durante un tiempo determinado
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inicia la actividad principal de la aplicación después del tiempo especificado
                Intent intent = new Intent(PantallaSplash.this, LoginMainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
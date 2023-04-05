package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class LoginMainActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    Button btnIngresarApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        // Instanciar el Fragment
        loginFragment = new LoginFragment();

        // Agregar el Fragment al layout del Activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();


    }
}
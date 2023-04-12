package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.ui.perfiluser.EditPerfilUser;
import com.example.myapplication.ui.perfiluser.perfiluser;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        String nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("DescripcionGrupoFragment")){
            DescripcionGrupoFragment miFragment = new DescripcionGrupoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("perfiluser")){
            perfiluser miFragment = new perfiluser();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("EditPerfilUser")){
            EditPerfilUser miFragment = new EditPerfilUser();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("CambioContrasenaFragment")){
            CambioContrasenaFragment miFragment = new CambioContrasenaFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }


    }
}
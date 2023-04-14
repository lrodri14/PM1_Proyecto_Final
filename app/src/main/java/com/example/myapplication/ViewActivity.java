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
        int usuarioId;

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

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("ListaUsuariosFragment")){
            ListaUsuariosFragment miFragment = new ListaUsuariosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("ListaArchivosGruposFragment")){
            ListaArchivosGruposFragment miFragment = new ListaArchivosGruposFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("grupoId", getIntent().getIntExtra("grupoId", 0));
            miFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("ListaGruposFragment")){
            ListaGruposFragment miFragment = new ListaGruposFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("Fragment_PerfilUsuarios")){
            usuarioId = getIntent().getIntExtra("idUsuario", 0);
            Fragment_PerfilUsuarios miFragment = new Fragment_PerfilUsuarios();
            Bundle bundle = new Bundle();
            bundle.putInt("id", usuarioId);
            miFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }

        nombreFragment = getIntent().getStringExtra("nombreFragment");
        if(nombreFragment != null && nombreFragment.equals("Fragment_PerfilAmigos")){
            usuarioId = getIntent().getIntExtra("idUsuario", 0);
            Fragment_PerfilAmigos miFragment = new Fragment_PerfilAmigos();
            Bundle bundle = new Bundle();
            bundle.putInt("id", usuarioId);
            miFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, miFragment).commit();
        }


    }
}
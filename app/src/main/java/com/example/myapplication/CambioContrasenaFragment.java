package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ui.perfiluser.EditPerfilUser;

public class CambioContrasenaFragment extends Fragment {

    Button btnVolverPerfil,btnCambiarContrasena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false);
        btnVolverPerfil = view.findViewById(R.id.btnVolverPerfil);
        btnCambiarContrasena = view.findViewById(R.id.btnCambiarContrasena);

        btnVolverPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "EditPerfilUser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);

            }
        });

        //Cambiará la contraseña y lo regresará al login
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPerfilUser fragmentEditPerfil = new EditPerfilUser(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, fragmentEditPerfil);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
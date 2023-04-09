package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CambioContrasenaLoginFragment extends Fragment {

    Button btnVolverLogin; //Para volver a la pantalla del login si no se desea cambiar la contraseña
    Button btnCambiarContrasena; //Cambiará la contraseña y lo regresará al login


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambio_contrasena_login, container, false);
        btnVolverLogin = view.findViewById(R.id.btnVolverPerfil);
        btnCambiarContrasena = view.findViewById(R.id.btnCambiarContrasena);

        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragmentLogin = new LoginFragment(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentLogin);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Cambiará la contraseña y lo regresará al login
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragmentLogin = new LoginFragment(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentLogin);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
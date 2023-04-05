package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RegistroFragment extends Fragment {

    TextView textLogin; //Ir a login por si ya se tiene una cuenta
    TextView textRegistrar; //Ir a la pantalla del correo de verificación para confirmar el registro

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        textLogin = view.findViewById(R.id.btnLogin);
        textRegistrar = view.findViewById(R.id.btnRegistro);


        //Evento clic para el botón "Ingresar" por si ya se tiene una cuenta
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragmentLogin = new LoginFragment(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentLogin);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        //Una vez llenado el registro, se envía el correo al hacer clic en este botón
        textRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificacionRegistroFragment fragmentCorreo = new VerificacionRegistroFragment(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentCorreo);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
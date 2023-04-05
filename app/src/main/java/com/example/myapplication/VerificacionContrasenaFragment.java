package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class VerificacionContrasenaFragment extends Fragment {

    Button btnCambiarContrasena; //Abrirá la pantalla donde permitirá al usuario cambiar la contraseña

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verificacion_contrasena, container, false);
        btnCambiarContrasena = view.findViewById(R.id.btnContinuar);

        //Abrirá la pantalla donde permitirá al usuario cambiar la contraseña
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambioContrasenaLoginFragment fragmentCambioContrasena = new CambioContrasenaLoginFragment(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentCambioContrasena);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;

    }
}
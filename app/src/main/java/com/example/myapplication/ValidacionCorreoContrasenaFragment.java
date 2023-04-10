package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ValidacionCorreoContrasenaFragment extends Fragment {

    Button btnContinuar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_correo_validacion, container, false);
        btnContinuar = view.findViewById(R.id.btnEnviar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificacionContrasenaFragment fragmentCorreoContrasena = new VerificacionContrasenaFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentCorreoContrasena);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
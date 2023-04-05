package com.example.myapplication;

import com.example.myapplication.MenuActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment {
    private TextView textRegistro, textCambiarPassword; // Para registrar si no está logueado el usuario
    //Y para cambiar contraseña por si este la olvidó
    private Button btnIngresar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        textRegistro = view.findViewById(R.id.btnRegistro);
        textCambiarPassword = view.findViewById(R.id.btnCambiarContrasena);
        btnIngresar = view.findViewById(R.id.btnIngresar);

        textRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroFragment fragmentRegistro = new RegistroFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentRegistro);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        textCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificacionContrasenaFragment fragmentCambiarContra = new VerificacionContrasenaFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentCambiarContra);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Evento clic para el botón "Ingresar" por si ya se tiene una cuenta
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ui.perfiluser.EditPerfilUser;

public class CambioContrasenaFragment extends Fragment {

    Button btnCambiarContrasena;
    ImageView btnVolverPerfil;
    EditText contra1, contra2, contra3;
    ImageButton btnVerPass1, btnVerPass2, btnVerPass3;
    boolean verPass = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false);
        btnVolverPerfil = view.findViewById(R.id.btnVolverLogin);
        btnCambiarContrasena = view.findViewById(R.id.btnCambiarContrasena);
        btnVerPass1 = view.findViewById(R.id.btnVerPass2);
        btnVerPass2= view.findViewById(R.id.btnVerPass3);
        btnVerPass3= view.findViewById(R.id.btnVerPass5);
        contra1 = view.findViewById(R.id.txtActualContrasena);
        contra2 = view.findViewById(R.id.txtPass3);
        contra3 = view.findViewById(R.id.txtPass2);

        btnVerPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra1.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass1.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
                } else {
                    contra1.setTransformationMethod(null);
                    btnVerPass1.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;
                }
            }
        });

        btnVerPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra2.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass2.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
                } else {
                    contra2.setTransformationMethod(null);
                    btnVerPass2.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;
                }
            }
        });
        btnVerPass3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra3.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass3.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
                } else {
                    contra3.setTransformationMethod(null);
                    btnVerPass3.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;
                }
            }
        });

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
                if (validateLoginFields()) {
                    String nombreFragment = "perfiluser";
                    Intent intent = new Intent(getContext(), ViewActivity.class);
                    intent.putExtra("nombreFragment", nombreFragment);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
    public boolean validateLoginFields() {
        String password1 = contra1.getText().toString().trim();
        String password2= contra2.getText().toString().trim();
        String password3= contra3.getText().toString().trim();

        if (TextUtils.isEmpty(password1)){
            contra1.setError( "Ingrese su actual contraseña");
            return false;
        }

        if (TextUtils.isEmpty(password2)) {
            contra2.setError("Ingrese su nueva contraseña");
            return false;
        }
        if (TextUtils.isEmpty(password3)) {
            contra3.setError("Ingrese la confirmación de su nueva contraseña");
            return false;
        }
        if (!password1.equals(password2)) {
            contra2.setError("Las contraseñas no coinciden");
            contra3.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }
}
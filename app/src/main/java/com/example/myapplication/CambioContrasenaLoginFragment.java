package com.example.myapplication;

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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CambioContrasenaLoginFragment extends Fragment {

    String id;
    ImageView btnVolverLogin; //Para volver a la pantalla del login si no se desea cambiar la contraseña
    Button btnCambiarContrasena; //Cambiará la contraseña y lo regresará al login
    EditText contra1, contra2;
    ImageButton btnVerPass1, btnVerPass2;
    TokenManager tokenManager;
    boolean verPass = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambio_contrasena_login, container, false);
        btnVolverLogin = view.findViewById(R.id.btnVolverMenu);
        btnCambiarContrasena = view.findViewById(R.id.btnCambiarContrasena);
        btnVerPass1 = view.findViewById(R.id.btnVerPass2);
        btnVerPass2= view.findViewById(R.id.btnVerPass3);
        contra1 = view.findViewById(R.id.txtActualContrasena);
        contra2 = view.findViewById(R.id.txtPass3);
        tokenManager = new TokenManager(getContext());

        if (getArguments() != null) {
            id = getArguments().getString("id");
            System.out.println(id);
        }

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
                if (validateLoginFields()) {
                    try {
                        cambiarContrasena(contra1.getText().toString(), id);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return view;
    }

    private void cambiarContrasena(String contra, String id) throws IOException, JSONException {
        String url = "https://www.api.katiosca.com/auth/restaurar_contra/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        tokenManager = new TokenManager(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contra", contra);
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    LoginFragment fragmentLogin = new LoginFragment(); //Se asigna el fragment que se abrirá
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragmentLogin);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    Toast.makeText(getActivity(), "Contraseña restaurada correctamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getActivity(), "Error al restaurar contraseña", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public boolean validateLoginFields() {
        String password1 = contra1.getText().toString().trim();
        String password2= contra2.getText().toString().trim();

        if (TextUtils.isEmpty(password1)){
            contra1.setError( "Ingrese su actual contraseña");
            return false;
        }

        if (TextUtils.isEmpty(password2)) {
            contra2.setError("Ingrese su nueva contraseña");
            return false;
        }

        if (!password1.equals(password2)) {
            contra1.setError("Las contraseñas no coinciden");
            contra2.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }
}
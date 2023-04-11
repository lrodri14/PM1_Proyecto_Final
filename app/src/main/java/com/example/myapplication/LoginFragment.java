package com.example.myapplication;

import com.example.myapplication.utilities.TokenManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class LoginFragment extends Fragment {
    private TextView textRegistro, textCambiarPassword; // Para registrar si no está logueado el usuario
    //Y para cambiar contraseña por si este la olvidó
    private Button btnIngresar;
    EditText user, contra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        textRegistro = view.findViewById(R.id.btnRegistro);
        textCambiarPassword = view.findViewById(R.id.btnCambiarContrasena);
        user = view.findViewById(R.id.txtUser);
        contra = view.findViewById(R.id.txtPass);
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
                ValidacionCorreoContrasenaFragment fragmentCorreoContrasena = new ValidacionCorreoContrasenaFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentCorreoContrasena);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Evento clic para el botón "Ingresar" por si ya se tiene una cuenta
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginFields()) {
                    login(user.getText().toString(), contra.getText().toString());
                }
            }
        });

        return view;
    }

    public boolean validateLoginFields() {
        String username = user.getText().toString().trim();
        String password = contra.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Ingrese su usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean login(String username, String password) {
        String url = "https://www.api.katiosca.com/auth/";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    String authToken = extractAuthToken(response);
                    TokenManager tokenManager = new TokenManager(getContext());
                    tokenManager.saveAuthToken(authToken);
                    Intent intent = new Intent(getContext(), MenuActivity.class);
                    startActivity(intent);
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorObject = new JSONObject(errorMessage);
                            String errorText = errorObject.getString("error");
                            System.out.println(errorText);
                            Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
        return false;
    }
    private String extractAuthToken(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");
            return data.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
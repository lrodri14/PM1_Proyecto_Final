package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RegistroFragment extends Fragment {

    TextView textLogin; //Ir a login por si ya se tiene una cuenta
    TextView textRegistrar; //Ir a la pantalla del correo de verificación para confirmar el registro
    EditText user, nombre, apellido, contra1, contra2, email;
    public Spinner spinnerCarrera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        user = view.findViewById(R.id.txtNumCuenta);
        nombre = view.findViewById(R.id.txtNombres);
        apellido = view.findViewById(R.id.txtApellidos);
        email = view.findViewById(R.id.txtCorreo);
        contra1 = view.findViewById(R.id.txtPass);
        contra2 = view.findViewById(R.id.txtPassConf);
        contra2 = view.findViewById(R.id.txtPassConf);
        textLogin = view.findViewById(R.id.btnLogin);
        textRegistrar = view.findViewById(R.id.btnRegistro);
        spinnerCarrera = view.findViewById(R.id.carreras);


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
                String usuario = user.getText().toString();
                String nombres = nombre.getText().toString();
                String apellidos = apellido.getText().toString();
                String correo = email.getText().toString();
                String pass1 = contra1.getText().toString();
                String pass2 = contra2.getText().toString();
                int carrera = spinnerCarrera.getSelectedItemPosition() + 1;
                registro(usuario, nombres, apellidos, pass1, pass2, correo, carrera);
            }
        });

        String url = "https://www.api.katiosca.com/carreras/";
        RequestQueue requestQueue;
        List<String> carreraLista = new ArrayList<>();

        // Create Volley request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        // Make request to server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String carrera = jsonObject.getString("nombre_carrera");
                            carreraLista.add(carrera);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, carreraLista);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerCarrera.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorObject = new JSONObject(errorMessage);
                            String errorText = errorObject.getString("error");
                            Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        // Add request to queue
        requestQueue.add(request);


        return view;
    }

    public boolean registro(String username, String nombre, String apellido, String password1,
                            String password2, String email, int carrera) {
        String url = "https://www.api.katiosca.com/auth/registro/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        System.out.println(carrera);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", nombre);
            jsonObject.put("last_name", apellido);
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("carrera", carrera);
            jsonObject.put("password1", password1);
            jsonObject.put("password2", password2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    String authToken = extractAuthToken(response);
                    TokenManager tokenManager = new TokenManager(getContext());
                    tokenManager.saveAuthToken(authToken);
                    LoginFragment fragmentCorreo = new LoginFragment(); //Se asigna el fragment que se abrirá
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragmentCorreo);
                    transaction.addToBackStack(null);
                    transaction.commit();
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorObject = new JSONObject(errorMessage);
                            String errorText = errorObject.getString("error");
                            System.out.println(errorText);
                            Toast.makeText(getContext(), "Error en la creacion del usuario", Toast.LENGTH_SHORT).show();
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
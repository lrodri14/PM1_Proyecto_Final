package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VerificacionContrasenaFragment extends Fragment {

    TokenManager tokenManager;
    EditText campoCodigo;
    Button btnCambiarContrasena; //Abrirá la pantalla donde permitirá al usuario cambiar la contraseña

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verificacion_contrasena, container, false);
        btnCambiarContrasena = view.findViewById(R.id.btnEnviar);
        tokenManager = new TokenManager(getContext());
        campoCodigo = view.findViewById(R.id.txtCorreo);

        //Abrirá la pantalla donde permitirá al usuario cambiar la contraseña
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    verificarCodigo(campoCodigo.getText().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
//                CambioContrasenaLoginFragment fragmentCambioContrasena = new CambioContrasenaLoginFragment(); //Se asigna el fragment que se abrirá
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container, fragmentCambioContrasena);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });

        return view;
    }

    private void verificarCodigo(String codigo) throws IOException, JSONException {
        String url = "https://www.api.katiosca.com/auth/verificar_codigo/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        tokenManager = new TokenManager(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codigo", codigo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        String id = data.getString("id");
                        CambioContrasenaLoginFragment fragmentCambioContrasena = new CambioContrasenaLoginFragment(); //Se asigna el fragment que se abrirá
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        fragmentCambioContrasena.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, fragmentCambioContrasena);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Toast.makeText(getActivity(), "Codigo Verificado", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "El codigo no existe", Toast.LENGTH_SHORT).show();
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
}
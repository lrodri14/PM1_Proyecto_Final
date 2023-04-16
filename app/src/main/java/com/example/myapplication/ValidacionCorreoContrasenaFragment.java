package com.example.myapplication;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ValidacionCorreoContrasenaFragment extends Fragment {

    Button btnContinuar;
    EditText correo;
    TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_correo_validacion, container, false);
        btnContinuar = view.findViewById(R.id.btnEnviar);
        correo = view.findViewById(R.id.txtCorreo);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    solicitarRestauracion(correo.getText().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return view;
    }

    private void solicitarRestauracion(String correo) throws IOException, JSONException {
        String url = "https://www.api.katiosca.com/auth/solicitar_restauracion/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        tokenManager = new TokenManager(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", correo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Toast.makeText(getActivity(), "Solicitud enviada", Toast.LENGTH_SHORT).show();
                    VerificacionContrasenaFragment fragmentCorreoContrasena = new VerificacionContrasenaFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragmentCorreoContrasena);
                    transaction.addToBackStack(null);
                    transaction.commit();
                },
                error -> {
                    Toast.makeText(getActivity(), "Error en la solicitud de restauracion", Toast.LENGTH_SHORT).show();
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
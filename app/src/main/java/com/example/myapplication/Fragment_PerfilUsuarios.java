package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_PerfilUsuarios extends Fragment {

    int userId;
    TextView usuario, nombre, correo, carrera, verificado;
    ImageView foto, btn_agg_amigo, btn_agg_grupo;
    TokenManager tokenManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__perfil_usuarios, container, false);

        tokenManager = new TokenManager(getContext());
        usuario = view.findViewById(R.id.userUsername);
        nombre = view.findViewById(R.id.nombreUsuario);
        correo = view.findViewById(R.id.correoElectronico);
        carrera = view.findViewById(R.id.nombreDeCarrera);
        verificado = view.findViewById(R.id.usuarioVerificado);
        foto = view.findViewById(R.id.foto);
        btn_agg_amigo = view.findViewById(R.id.btn_aggamigo);
        btn_agg_grupo = view.findViewById(R.id.btn_agggrupo);

        if (getArguments() != null) {
            userId = getArguments().getInt("id");
            System.out.println(userId);
        }

        btn_agg_amigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seguirUsuario();
                String nombreFragment = "ListaUsuariosFragment";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });

        btn_agg_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreFragment = "ListaGruposFragment";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });


        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("usuario");
                        JSONObject ingenieria = data.getJSONObject("carrera");
                        usuario.setText(user.getString("username"));
                        nombre.setText(user.getString("first_name") + " " + user.getString("last_name"));
                        correo.setText(user.getString("email"));
                        carrera.setText(ingenieria.getString("nombre_carrera"));

                        if (data.getString("verificado") == "false"){
                            verificado.setText("Usuario No Verificado");
                        }else{
                            verificado.setText("Usuario Verificado");
                        }

                        if (data.getString("foto_de_perfil") != "null"){
                            Picasso.get().load("https://www.api.katiosca.com" + data.getString("foto_de_perfil")).into(foto);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(request);

        return view;
    }

    public void seguirUsuario(){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/seguir";

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("usuario_seguido", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        Toast.makeText(getContext(), "Usuario Seguido!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al intentar seguir usuario.", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(getContext(), "Error al intentar seguir usuario.", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        queue.add(request);
    }

}
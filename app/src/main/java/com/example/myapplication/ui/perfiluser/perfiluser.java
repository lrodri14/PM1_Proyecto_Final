package com.example.myapplication.ui.perfiluser;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.ViewActivity;
import com.example.myapplication.utilities.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class perfiluser extends Fragment {

    ImageView btnEdit;
    TextView usuario, nombre, correo, carrera, verificado;
    ImageView foto;

    TokenManager tokenManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfiluser, container, false);
        btnEdit = view.findViewById(R.id.btnEdit);

        tokenManager = new TokenManager(getContext());
        usuario = view.findViewById(R.id.usuario);
        nombre = view.findViewById(R.id.nombre);
        correo = view.findViewById(R.id.correo);
        carrera = view.findViewById(R.id.carrera);
        verificado = view.findViewById(R.id.verificado);
        foto = view.findViewById(R.id.foto);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "EditPerfilUser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/personal";
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
}
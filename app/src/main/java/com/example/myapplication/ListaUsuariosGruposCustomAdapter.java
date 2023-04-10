package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaUsuariosGruposCustomAdapter extends ArrayAdapter<Usuario> {
    private Context context;
    private int resource;
    private List<Usuario> usuarios;
    private String grupoId;

    TokenManager tokenManager;

    public ListaUsuariosGruposCustomAdapter(Context context, int resource, List<Usuario> usuarios, String grupoId) {
        super(context, resource, usuarios);
        this.context = context;
        this.resource = resource;
        this.usuarios = usuarios;
        this.grupoId = grupoId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        Usuario usuario = getItem(position);
        tokenManager = new TokenManager(getContext());

        ImageView userImage = view.findViewById(R.id.user_image);
        TextView userName = view.findViewById(R.id.user_name);
        TextView userCareer = view.findViewById(R.id.user_career);
        ImageView userIcon = view.findViewById(R.id.user_icon);

        userImage.setImageResource(usuario.getImageResourceId());
        userName.setText(usuario.getName());
        userCareer.setText(usuario.getCareer());
        userIcon.setImageResource(usuario.getIconResourceId());

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarIntegrante(getContext(), grupoId, usuario.getId(), usuario);
            }
        });

        return view;
    }

    public void agregarIntegrante(Context context, String grupoId, int userId, Usuario usuario) {
        String url = "https://www.api.katiosca.com/grupos/" + grupoId + "/agregar";
        JSONObject requestBody = new JSONObject();
        try {
            JSONArray integrantesArray = new JSONArray();
            integrantesArray.put(userId);
            requestBody.put("integrantes", integrantesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        Toast.makeText(context, "Usuario: " + usuario.getName() + " agregado al grupo", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        JSONObject errorRespuesta = new JSONObject(new String(error.networkResponse.data));
                        String errorMensaje = errorRespuesta.getString("error");
                        Toast.makeText(context, "Error: " + errorMensaje, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Hubo un error al agregar al usuario al grupo", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

}



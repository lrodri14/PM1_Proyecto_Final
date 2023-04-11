package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaAmigosCustomAdapter extends ArrayAdapter<Usuario> {

    private Context Context;
    private int Resource;
    TokenManager tokenManager = new TokenManager(getContext());

    public ListaAmigosCustomAdapter(Context context, int resource, List<Usuario> objects) {
        super(context, resource, objects);
        Context = context;
        Resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(Context);
            view = inflater.inflate(Resource, parent, false);
        }

        Usuario usuario = getItem(position);

        ImageView friendImage = view.findViewById(R.id.friend_image);
        TextView friendIName = view.findViewById(R.id.friend_name);
        TextView friendICareer = view.findViewById(R.id.friend_career);
        ImageView friendIIcon = view.findViewById(R.id.friend_icon);

        friendImage.setImageResource(usuario.getImageResourceId());
        friendIName.setText(usuario.getName());
        friendICareer.setText(usuario.getCareer());
        friendIIcon.setImageResource(usuario.getIconResourceId());

        friendIIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarUsuario(usuario.getId(), view);
            }
        });

        return view;
    }

    public void eliminarUsuario(int id, View view) {
        String url = "https://api.katiosca.com/perfiles/seguir/" + id;
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    ListaAmigosFragment listaAmigosFragment = new ListaAmigosFragment();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_main, listaAmigosFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                },
                error -> {
                    // Handle error response here
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorResponse = new String(error.networkResponse.data);
                        try {
                            JSONObject errorObj = new JSONObject(errorResponse);
                            // Handle specific error based on the error response JSON
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ) {
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

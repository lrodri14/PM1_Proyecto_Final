package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaAmigosFragment extends Fragment {

    private ListView FriendListView;
    private TextView cancelText;
    private ListaAmigosCustomAdapter adapter;
    private EditText searchView;
    private ImageView iconoCancelar;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_amigos, container, false);

        FriendListView = view.findViewById(R.id.friend_list);
        searchView = view.findViewById(R.id.search_view_edit_text);
        cancelText = view.findViewById(R.id.cancel_text);

        List<Usuario> friendList = new ArrayList<>();
        tokenManager = new TokenManager(getContext());
        adapter = new ListaAmigosCustomAdapter(getContext(), R.layout.lista_item_amigo, friendList);
        FriendListView.setAdapter(adapter);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/personal";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("siguiendo");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("usuario_seguido");
                            Integer id = jsonObject.getJSONObject("usuario").getInt("id");
                            String firstName = jsonObject.getJSONObject("usuario").getString("first_name");
                            String lastName = jsonObject.getJSONObject("usuario").getString("last_name");
                            String carreraNombre = jsonObject.getJSONObject("carrera").getString("nombre_carrera");
                            friendList.add(new Usuario(id, R.drawable.perfil, firstName + " " + lastName, carreraNombre, R.drawable.quitarusuario));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Error
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        queue.add(request);

        return view;
    }
}

package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaChatFragment  extends Fragment {

    private ListView mChatListView;
    private ListaChatCustomAdapter mChatListAdapter;
    private ArrayList<ChatGroup> mChatGroups;
    private TokenManager tokenManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_lista, container, false);
        tokenManager = new TokenManager(getContext());
        mChatListView = rootView.findViewById(R.id.chat_list_view);
        cargarGrupos();
        return rootView;
    }

    public void cargarGrupos() {
        String url = "https://www.api.katiosca.com/grupos/lista";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            mChatGroups = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject chatGroupJson = data.getJSONObject(i);
                                String nombre = chatGroupJson.getString("nombre");
                                JSONArray miembros = chatGroupJson.getJSONArray("usuarios");
                                List<String> members = new ArrayList<>();

                                for (int j = 0; j < miembros.length(); j++) {
                                    JSONObject miembro = miembros.getJSONObject(j);
                                    members.add(miembro.getString("username"));
                                }

                                ChatGroup chatGroup = new ChatGroup(nombre, members);
                                mChatGroups.add(chatGroup);
                            }

                            mChatListAdapter = new ListaChatCustomAdapter(getContext(), mChatGroups);
                            mChatListView.setAdapter(mChatListAdapter);

                            mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    ChatGroup chatGroup = mChatGroups.get(position);
                                    Intent intent = new Intent(getActivity(), ConversacionActivity.class);
                                    intent.putExtra("nombre", chatGroup.getName());
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error al mostrar los grupos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error cargando los grupos", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }


}



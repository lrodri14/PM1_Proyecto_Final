package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ui.perfiluser.EditPerfilUser;
import com.example.myapplication.utilities.TokenManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListaUsuariosFragment extends Fragment {

    private ListView UserListView;
    private TextView cancelText;

    private ListaUsuariosCustomAdapter adapter;
    private EditText searchView;

    TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_usuarios, container, false);

        UserListView = view.findViewById(R.id.user_list);
        searchView = view.findViewById(R.id.search_view_edit_text);
        cancelText = view.findViewById(R.id.cancel_text);
        tokenManager = new TokenManager(getContext());

        extraerUsuarios();

        /* Listener para el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Ocultar el teclado
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

         // Listener para el TextView de Cancelar
        cancelText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            search

       */

        UserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Usuario usuario = (Usuario) adapterView.getItemAtPosition(position);
                int usuarioId = usuario.getId();
                Fragment_PerfilUsuarios fragmentPerfilUsuarios = new Fragment_PerfilUsuarios();
                Bundle bundle = new Bundle();
                bundle.putInt("id", usuarioId);
                fragmentPerfilUsuarios.setArguments(bundle);

                String nombreFragment = "Fragment_PerfilUsuarios";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                intent.putExtra("idUsuario", usuarioId);
                startActivity(intent);

                /*
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, fragmentPerfilUsuarios);
                transaction.addToBackStack(null);
                transaction.commit();
                */

            }
        });

        return view;
    }

    private void extraerUsuarios() {
        String url = "https://www.api.katiosca.com/perfiles/lista";
        List<Usuario> userList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject usuario = data.getJSONObject(i);

                                int id = usuario.getInt("id");
                                String nombre = usuario.getJSONObject("usuario").getString("first_name");
                                String apellido = usuario.getJSONObject("usuario").getString("last_name");
                                String carrera = usuario.getJSONObject("carrera").getString("nombre_carrera");
                                int imageResourceId = R.drawable.perfil;
                                int iconResourceId = R.drawable.agregar_usuario;

                                String nombreCompleto = nombre + " " + apellido;

                                Usuario u = new Usuario(id, imageResourceId, nombreCompleto, carrera, iconResourceId);
                                userList.add(u);

                                ListaUsuariosCustomAdapter adapter = new ListaUsuariosCustomAdapter(getContext(), R.layout.lista_item_buscar_usuario_grupo, userList, id);
                                UserListView.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        queue.add(request);
    }

}

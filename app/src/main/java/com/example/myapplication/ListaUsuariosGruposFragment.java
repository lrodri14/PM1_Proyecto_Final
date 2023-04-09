package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuariosGruposFragment extends Fragment {

    private ListView UserListView;
    private TextView cancelText;

    private ListaUsuariosCustomAdapter adapter;
    private EditText searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_buscar_usuarios_grupo, container, false);

        UserListView = view.findViewById(R.id.user_list);
        searchView = view.findViewById(R.id.search_view_edit_text);
        cancelText = view.findViewById(R.id.cancel_text);

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







        List<Usuario> userList = new ArrayList<>();
        userList.add(new Usuario(R.drawable.usuario1, "Estefany Gonzales ", "Ingeniería en Computacion", R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario2, "Elsy Amaya", "Ingeniería en Computacion", R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario3, "Fabiana Rodriguez", "Ingeniería en Computacion",  R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario4, "Jorge Meraz", "Ingeniería en Computacion", R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario1, "Luis Rodriguez", "Ingeniería en Computacion",  R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario2, "Armando Lozano", "Ingeniería en Computacion",  R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario3, "Laura García", "Ingeniería Industrial", R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario4, "Javier Milei", "Master Phu Economia", R.drawable.agregar_usuario));
        userList.add(new Usuario(R.drawable.usuario4, "Stephany Meraz", "Ingenieria Industrial",  R.drawable.agregar_usuario));


        ListaUsuariosGruposCustomAdapter adapter = new ListaUsuariosGruposCustomAdapter(getContext(), R.layout.lista_item_buscar_usuario_grupo, userList);
        UserListView.setAdapter(adapter);

        return view;
    }

}

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

public class ListaAmigosFragment extends Fragment {

    private ListView FriendListView;
    private TextView cancelText;
    private ListaAmigosCustomAdapter adapter;
    private EditText searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_amigos, container, false);

        FriendListView = view.findViewById(R.id.friend_list);
        searchView = view.findViewById(R.id.search_view_edit_text);
        cancelText = view.findViewById(R.id.cancel_text);

        List<Amigos> friendList = new ArrayList<>();
        friendList.add(new Amigos(R.drawable.perfil, "Francisco Morazan ", "Lic. en Derecho", R.drawable.quitarusuario));
        friendList.add(new Amigos(R.drawable.perfil, "Abraham Lincoln", "Ingenieria en Electricidad", R.drawable.quitarusuario));
        friendList.add(new Amigos(R.drawable.perfil, "George Whashintong", "Ingenieria Industrial", R.drawable.quitarusuario));
        friendList.add(new Amigos(R.drawable.perfil, "Joao Da Silva", "Lic. en Economia", R.drawable.quitarusuario));

        ListaAmigosCustomAdapter adapter = new ListaAmigosCustomAdapter(getContext(), R.layout.lista_item_amigo, friendList);
        FriendListView.setAdapter(adapter);

        return view;
    }


}

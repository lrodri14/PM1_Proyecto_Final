package com.example.myapplication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListaGruposFragment extends Fragment {

    private ListView GroupListView;
    private TextView nombreGrupo;
    private ListaGruposCustomAdapter adapter;
    private ImageView editarNombreGrupo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_grupos, container, false);

        GroupListView = view.findViewById(R.id.group_list);

        List<ListaGrupos> groupList = new ArrayList<>();
        groupList.add(new ListaGrupos(R.drawable.medio, "Programación Móvil I", R.drawable.agregar_usuario));
        groupList.add(new ListaGrupos(R.drawable.medio, "Inteligencia Artificial", R.drawable.agregar_usuario));
        groupList.add(new ListaGrupos(R.drawable.medio, "Ingeniería de Software II", R.drawable.agregar_usuario));
        groupList.add(new ListaGrupos(R.drawable.medio, "Ecuciones Diferenciales", R.drawable.agregar_usuario));

        ListaGruposCustomAdapter adapter = new ListaGruposCustomAdapter(getContext(), R.layout.lista_item_grupos, groupList);
        GroupListView.setAdapter(adapter);




        return view;
    }


}

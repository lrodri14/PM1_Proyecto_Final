package com.example.myapplication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListaArchivosGruposFragment extends Fragment {

    private ListView FileGroupListView;
    private TextView nombreArchivo;
    private ListaArchivosGruposCustomAdapter adapter;
    private ImageView editarNombreGrupo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_archivos_grupos, container, false);

        FileGroupListView = view.findViewById(R.id.file_group_list);

        List<ListaArchivosGrupos> fileList = new ArrayList<>();
        fileList.add(new ListaArchivosGrupos(R.drawable.documento, "Archivo 1", R.drawable.download_icon));
        fileList.add(new ListaArchivosGrupos(R.drawable.documento, "Archivo 2", R.drawable.download_icon));

        ListaArchivosGruposCustomAdapter adapter = new ListaArchivosGruposCustomAdapter(getContext(), R.layout.lista_item_archivos_grupos, fileList);
        FileGroupListView.setAdapter(adapter);


        return view;
    }


}

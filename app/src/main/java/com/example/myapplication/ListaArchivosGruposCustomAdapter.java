package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListaArchivosGruposCustomAdapter extends ArrayAdapter<ListaArchivosGrupos> {

    private Context Context;

    private List<ListaArchivosGrupos> fileList;

    private int Resource;

    public ListaArchivosGruposCustomAdapter(Context context, int resource, List<ListaArchivosGrupos> objects) {
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

        ListaArchivosGrupos listaArchivosGrupos = getItem(position);

        ImageView fileImage = view.findViewById(R.id.file_image);
        ImageView download = view.findViewById(R.id.descargar_archivo);
        TextView fileName = view.findViewById(R.id.file_name);

        fileImage.setImageResource(listaArchivosGrupos.getImageResourceId());
        fileName.setText(listaArchivosGrupos.getName());
        download.setImageResource(listaArchivosGrupos.getIconResourceId());


        return view;



    }
}

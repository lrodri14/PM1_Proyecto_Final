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

public class ListaGruposCustomAdapter extends ArrayAdapter<ListaGrupos> {

    private Context Context;
    private int Resource;

    public ListaGruposCustomAdapter(Context context, int resource, List<ListaGrupos> objects ) {
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

        ListaGrupos listaGrupos = getItem(position);

        ImageView groupImage = view.findViewById(R.id.group_image);
        ImageView agregar_usuario = view.findViewById(R.id.agregar_usuario);
        TextView groupName = view.findViewById(R.id.group_name);

        groupImage.setImageResource(listaGrupos.getImageResourceId());
        groupName.setText(listaGrupos.getName());
        agregar_usuario.setImageResource(listaGrupos.getIconResourceId());


        return view;



    }
}

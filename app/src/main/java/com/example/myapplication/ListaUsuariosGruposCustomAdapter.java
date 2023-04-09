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

public class ListaUsuariosGruposCustomAdapter extends ArrayAdapter<Usuario> {

    private Context Context;
    private int Resource;

    public ListaUsuariosGruposCustomAdapter(Context context, int resource, List<Usuario> objects) {
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

        ImageView userImage = view.findViewById(R.id.user_image);
        TextView userName = view.findViewById(R.id.user_name);
        TextView userCareer = view.findViewById(R.id.user_career);
        ImageView userIcon = view.findViewById(R.id.user_icon);

        userImage.setImageResource(usuario.getImageResourceId());
        userName.setText(usuario.getName());
        userCareer.setText(usuario.getCareer());
        userIcon.setImageResource(usuario.getIconResourceId());

        return view;

     //Filtro de Busqueda
      /*  public void filter(String text) {
            usuario.clear();
            if (text.isEmpty()) {
                users.addAll(originalUsers);
            } else {
                text = text.toLowerCase();
                for (User user : originalUsers) {
                    if (user.getName().toLowerCase().contains(text) || user.getCareer().toLowerCase().contains(text)) {
                        users.add(user);
                    }
                }
            }
            notifyDataSetChanged();
        }*/

    }


}



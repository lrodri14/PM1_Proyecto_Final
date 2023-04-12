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

public class DescripcionGrupoCustomAdapter extends ArrayAdapter<DescripcionGrupo> {

    private Context Context;
    private int Resource;

    public DescripcionGrupoCustomAdapter(Context context, int resource, List<DescripcionGrupo> objects) {
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

        DescripcionGrupo descripParticipGrupo = getItem(position);

        ImageView friendImage = view.findViewById(R.id.friend_image);
        TextView friendIName = view.findViewById(R.id.friend_name);
        TextView friendICareer = view.findViewById(R.id.friend_career);
        ImageView friendIIcon = view.findViewById(R.id.friend_icon);

        friendImage.setImageResource(descripParticipGrupo.getImageResourceId());
        friendIName.setText(descripParticipGrupo.getName());
        friendICareer.setText(descripParticipGrupo.getCareer());
        friendIIcon.setImageResource(descripParticipGrupo.getIconResourceId());



        return view;



    }
}

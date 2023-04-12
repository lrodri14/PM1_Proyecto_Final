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

public class DescripcionGrupoFragment extends Fragment {

    private ListView ParticipantsListView;
    private TextView nombreGrupo;
    private DescripcionGrupoCustomAdapter adapter;
    private ImageView editarNombreGrupo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion_grupo, container, false);

        ParticipantsListView = view.findViewById(R.id.participants_list);
        editarNombreGrupo = view.findViewById(R.id.editNombreGrupo);
        nombreGrupo = view.findViewById(R.id.nombreGrupo);

        editarNombreGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarNombreGrupoFragment fragmentNombreGrupo = new CambiarNombreGrupoFragment();
                fragmentNombreGrupo.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
                fragmentNombreGrupo.show(getActivity().getSupportFragmentManager(), "CambiarNombreGrupoFragment");
            }
        });


        List<DescripcionGrupo> participantList = new ArrayList<>();
        participantList.add(new DescripcionGrupo(R.drawable.amigo1, "Francisco Morazan ", "Lic. en Derecho", R.drawable.friendicon));
        participantList.add(new DescripcionGrupo(R.drawable.amigo2, "Abraham Lincoln", "Ingenieria en Electricidad", R.drawable.friendicon));
        participantList.add(new DescripcionGrupo(R.drawable.amigo3, "George Whashintong", "Ingenieria Industrial", R.drawable.friendicon));
        participantList.add(new DescripcionGrupo(R.drawable.amigo4, "Joao Da Silva", "Lic. en Economia", R.drawable.friendicon));

        DescripcionGrupoCustomAdapter adapter = new DescripcionGrupoCustomAdapter(getContext(), R.layout.lista_item_participantes, participantList);
        ParticipantsListView.setAdapter(adapter);




        return view;
    }


}

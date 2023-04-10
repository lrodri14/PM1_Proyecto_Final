package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NotificacionFragment extends Fragment {

    private ListView listView;
    private NotificacionCustomAdapter adapter;
    private List<Notificacion> notifications;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificacion, container, false);
        listView = view.findViewById(R.id.listView);
        notifications = new ArrayList<>();
        notifications.add(new Notificacion("Programacion Web II", "@Juan Fernandez", "Ha enviado un archivo al grupo", R.drawable.notificacion, "10:00", "08/04/2023"));
        notifications.add(new Notificacion("Solicitud de Amistad", "@Diego Enriquez", "Te ha enviado una solicitud de amistad", R.drawable.notificacion, "11:00", "09/04/2023"));
        notifications.add(new Notificacion("Programacion Movil I", "@Oscar Peraz", "Ha enviado un archivo al grupo", R.drawable.notificacion, "12:00", "10/04/2023"));
        notifications.add(new Notificacion("Solicitud de Amistad", "@Diana Armendia", "Te ha enviado una solicitud de amistad", R.drawable.notificacion, "01:00", "11/04/2023"));
        adapter = new NotificacionCustomAdapter(getContext(), notifications);
        listView.setAdapter(adapter);
        return view;
    }
}

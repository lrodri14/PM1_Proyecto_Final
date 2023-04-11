package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_PerfilUsuarios extends Fragment {

    int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            userId = getArguments().getInt("id");
            System.out.println(userId);
        }

        View view = inflater.inflate(R.layout.fragment__perfil_usuarios, container, false);
        return view;
    }

}
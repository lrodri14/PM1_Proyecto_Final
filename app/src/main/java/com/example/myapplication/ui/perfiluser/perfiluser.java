package com.example.myapplication.ui.perfiluser;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.MenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegistroFragment;
import com.example.myapplication.ViewActivity;


public class perfiluser extends Fragment {

    ImageView btnEdit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfiluser, container, false);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "EditPerfilUser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });




        return view;
    }
}
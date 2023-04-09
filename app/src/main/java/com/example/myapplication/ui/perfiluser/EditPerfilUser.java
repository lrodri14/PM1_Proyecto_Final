package com.example.myapplication.ui.perfiluser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.CambioContrasenaFragment;
import com.example.myapplication.R;

public class EditPerfilUser extends Fragment {

    ImageView btnCambiarPass;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editperfiluser, container, false);
        btnCambiarPass = view.findViewById(R.id.btnCambiarPass);
        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CambioContrasenaFragment fragmentEditPerfil = new CambioContrasenaFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, fragmentEditPerfil);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }


}
package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

public class CrearGrupoFragment extends DialogFragment {

    private EditText txtNombreGrupo;
    private Button btnGuardar;
    private Button btnCancelar;

    public CrearGrupoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_grupo, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        // Obtener referencia a los elementos de la vista
        txtNombreGrupo = view.findViewById(R.id.txtNombreGrupo);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        // Configurar el evento click del botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre del grupo y hacer algo con él
                String nombreGrupo = txtNombreGrupo.getText().toString();
                // Aquí puedes guardar el nombre del grupo en una base de datos o hacer cualquier otra acción necesaria
                // ...
                // Cerrar el fragment
                dismiss();
                ListaUsuariosGruposFragment fragmenUsuariosGrupos = new ListaUsuariosGruposFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, fragmenUsuariosGrupos);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        // Configurar el evento click del botón Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el fragment
                dismiss();
            }
        });

        // Configurar el tamaño de la ventana modal
        getDialog().getWindow().setLayout(900, 900);

        return view;
    }

}

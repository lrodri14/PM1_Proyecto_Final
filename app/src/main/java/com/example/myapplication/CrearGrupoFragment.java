package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                crearGrupo(nombreGrupo);
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
        getDialog().getWindow().setLayout(800, 800);

        return view;
    }

    public void crearGrupo(String nombre) {
        String url = "https://www.api.katiosca.com/grupos/";

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("nombre", nombre);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String id = data.getString("id");
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            ListaUsuariosGruposFragment fragmenUsuariosGrupos = new ListaUsuariosGruposFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("grupo_id", id);
                            fragmenUsuariosGrupos.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment_content_main, fragmenUsuariosGrupos);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            try {
                                JSONObject errorObject = new JSONObject(new String(response.data));
                                String errorMessage = errorObject.optString("error");
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error creando el grupo", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            error.printStackTrace();
                            Toast.makeText(getContext(), "Error creando el grupo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                TokenManager tokenManager = new TokenManager(Objects.requireNonNull(getContext()));
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

}
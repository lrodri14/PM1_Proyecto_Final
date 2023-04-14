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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DescripcionGrupoFragment extends Fragment {

    ListView ParticipantsListView;
    TextView nombreGrupo;
    DescripcionGrupoCustomAdapter adapter;
    private ImageView editarNombreGrupo;
    int grupo_id;
    TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion_grupo, container, false);

        ParticipantsListView = view.findViewById(R.id.participants_list);
        editarNombreGrupo = view.findViewById(R.id.editNombreGrupo);
        nombreGrupo = view.findViewById(R.id.nombreGrupo);
        tokenManager = new TokenManager(getContext());

        Bundle args = getArguments();
        if (args != null) {
            grupo_id = args.getInt("grupoId");
        }

        editarNombreGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarNombreGrupoFragment fragmentNombreGrupo = new CambiarNombreGrupoFragment();
                fragmentNombreGrupo.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
                fragmentNombreGrupo.show(getActivity().getSupportFragmentManager(), "CambiarNombreGrupoFragment");
            }
        });


        List<DescripcionGrupo> participantList = new ArrayList<>();
        AtomicReference<DescripcionGrupoCustomAdapter> adapter = new AtomicReference<>(new DescripcionGrupoCustomAdapter(getContext(), R.layout.lista_item_participantes, participantList));

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/grupos/" +  grupo_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String nombre = jsonObject.getString("nombre");
                        nombreGrupo.setText(nombre);
                        JSONArray jsonArray = jsonObject.getJSONArray("usuarios");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            String firstName = jsonObj.getString("first_name");
                            String lastName = jsonObj.getString("last_name");
                            String correo = jsonObj.getString("email");
                            participantList.add(new DescripcionGrupo(R.drawable.amigo2, firstName + " " + lastName, correo, R.drawable.friendicon));
                        }
                        adapter.set(new DescripcionGrupoCustomAdapter(getContext(), R.layout.lista_item_participantes, participantList));
                        ParticipantsListView.setAdapter(adapter.get());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Error
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        queue.add(request);




        return view;
    }


}

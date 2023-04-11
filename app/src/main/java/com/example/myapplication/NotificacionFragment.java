package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class NotificacionFragment extends Fragment {

    private ListView listView;
    private NotificacionCustomAdapter adapter;
    private List<Notificacion> notifications;
    TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificacion, container, false);
        listView = view.findViewById(R.id.listView);
        notifications = new ArrayList<>();
        tokenManager = new TokenManager(getContext());

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/notificaciones";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String titulo = jsonObject.getString("titulo");
                            String mensaje = jsonObject.getString("mensaje");
                            String fecha = jsonObject.getString("fecha");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date = sdf.parse(fecha);
                            SimpleDateFormat dateSdf = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
                            String dateStr = dateSdf.format(date);
                            String timeStr = timeSdf.format(date);
                            String usuario = jsonObject.getJSONObject("usuario_creador").getString("username");
                            notifications.add(new Notificacion(titulo, "@" + usuario, mensaje, R.drawable.notificacion, timeStr, dateStr));
                        }
                        adapter = new NotificacionCustomAdapter(getContext(), notifications);
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> {
            // Handle error
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(request);

        return view;
    }
}

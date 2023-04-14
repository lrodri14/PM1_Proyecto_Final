package com.example.myapplication;
import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListaArchivosGruposFragment extends Fragment {

    int grupo_id;
    private ListView FileGroupListView;
    private TextView nombreArchivo;
    private ListaArchivosGruposCustomAdapter adapter;
    private ImageView editarNombreGrupo;
    private ArrayList<ListaArchivosGrupos> fileList;
    TokenManager tokenManager;

    private class descargarTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urls) {
            String url = urls[0];
            descargarArchivo(url, "test.pdf");
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_archivos_grupos, container, false);
        FileGroupListView = view.findViewById(R.id.file_group_list);
        tokenManager = new TokenManager(getContext());
        fileList = new ArrayList<>();

        Bundle args = getArguments();
        if (args != null) {
            grupo_id = args.getInt("grupoId");
        }

        extraerArchivos();

        FileGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListaArchivosGrupos grupo = fileList.get(position);
                String nombre = grupo.getName();
                mostrarPDF("https://www.api.katiosca.com" + nombre);
//                new descargarTask().execute(nombre);
            }
        });

        return view;
    }

    private void extraerArchivos() {
        String url = "https://www.api.katiosca.com/archivos/" + grupo_id;
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject usuario = data.getJSONObject(i);
                                String nombre = usuario.getString("archivo");
                                fileList.add(new ListaArchivosGrupos(R.drawable.documento, nombre, R.drawable.download_icon));
                                ListaArchivosGruposCustomAdapter adapter = new ListaArchivosGruposCustomAdapter(getContext(), R.layout.lista_item_archivos_grupos, fileList);
                                FileGroupListView.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        queue.add(request);
    }

    public void mostrarPDF(String url) {
        WebView webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(webView);
        builder.show();
    }

    public void descargarArchivo(String url, String fileName) {
        try {
            url = "https://www.api.katiosca.com" + url;
            URL urlDescarga = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlDescarga.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(fileName);

                byte[] buffer = new byte[1024];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            } else {
                throw new Exception("Error al descargar grupo: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ui.perfiluser.EditPerfilUser;
import com.example.myapplication.utilities.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CambioContrasenaFragment extends Fragment {

    Button btnCambiarContrasena;
    ImageView btnVolverPerfil;
    EditText contra1, contra2, contra3;
    ImageButton btnVerPass1, btnVerPass2, btnVerPass3;
    boolean verPass = false;
    TokenManager tokenManager;

     /*  private class UploadFileTask extends AsyncTask<Object, Void, Void> {

     protected Void doInBackground(Object... objects) {
            String nombre = (String) objects[0];
            String apellido = (String) objects[1];
            String correo = (String) objects[2];

            try {
                editarPerfil(nombre, apellido, correo);
                String nombreFragment = "perfiluser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tokenManager = new TokenManager(getContext());
        View view = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false);
        btnVolverPerfil = view.findViewById(R.id.btnVolverMenu);
        btnCambiarContrasena = view.findViewById(R.id.btnCambiarContrasena);
        btnVerPass1 = view.findViewById(R.id.btnVerPass2);
        btnVerPass2= view.findViewById(R.id.btnVerPass3);
        btnVerPass3= view.findViewById(R.id.btnVerPass5);
        contra1 = view.findViewById(R.id.txtActualContrasena);
        contra2 = view.findViewById(R.id.txtPass3);
        contra3 = view.findViewById(R.id.txtPass2);

        btnVerPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actual = contra1.getText().toString();
                String nueva = contra2.getText().toString();
                String confirnew=contra3.getText().toString();
            //    new UploadFileTask().execute(actual, nueva, confirnew);
            }
        });
        btnVerPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra1.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass1.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
                } else {
                    contra1.setTransformationMethod(null);
                    btnVerPass1.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;
                }
            }
        });

        btnVerPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra2.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass2.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
                } else {
                    contra2.setTransformationMethod(null);
                    btnVerPass2.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;
                }
            }
        });
        btnVerPass3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra3.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass3.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
                } else {
                    contra3.setTransformationMethod(null);
                    btnVerPass3.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;
                }
            }
        });

        btnVolverPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "EditPerfilUser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);

            }
        });

        //Cambiará la contraseña y lo regresará al login
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginFields()) {
                    String nombreFragment = "perfiluser";
                    Intent intent = new Intent(getContext(), ViewActivity.class);
                    intent.putExtra("nombreFragment", nombreFragment);
                    startActivity(intent);
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/personal";
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("usuario");
                        contra1.setText(user.getString("password"));

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
    public boolean validateLoginFields() {
        String password1 = contra1.getText().toString().trim();
        String password2= contra2.getText().toString().trim();
        String password3= contra3.getText().toString().trim();

        if (TextUtils.isEmpty(password1)){
            contra1.setError( "Ingrese su actual contraseña");
            return false;
        }

        if (TextUtils.isEmpty(password2)) {
            contra2.setError("Ingrese su nueva contraseña");
            return false;
        }
        if (TextUtils.isEmpty(password3)) {
            contra3.setError("Ingrese la confirmación de su nueva contraseña");
            return false;
        }
        if (!password2.equals(password3)) {
            contra2.setError("Las contraseñas no coinciden");
            contra3.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }
  /*  private void editarPerfil(String actual, String nueva, String confirnew) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        // create a MultipartBody.Builder to build the request body
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("old_password", actual)
                .addFormDataPart("new_password1", nueva)
                .addFormDataPart("new_password2", confirnew);




        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://www.api.katiosca.com/auth/cambiar_contra/")
                .header("Authorization", "Token " + tokenManager.getAuthToken())
                .build();


        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);

        } else {

        }*/
    }



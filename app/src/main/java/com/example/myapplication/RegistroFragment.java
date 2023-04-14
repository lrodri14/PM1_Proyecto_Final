package com.example.myapplication;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDeepLinkBuilder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RegistroFragment extends Fragment {
    private static final String MY_CHANNEL_ID = "myChannel";

    TextView textLogin; //Ir a login por si ya se tiene una cuenta
    TextView textRegistrar; //Ir a la pantalla del correo de verificación para confirmar el registro
    EditText user, nombre, apellido, contra1, contra2, email;
    ImageButton btnVerPass;
    public Spinner spinnerCarrera;
    boolean verPass = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        user = view.findViewById(R.id.txtNumCuenta);
        nombre = view.findViewById(R.id.txtNombres);
        apellido = view.findViewById(R.id.txtApellidos);
        email = view.findViewById(R.id.txtCorreo);
        contra1 = view.findViewById(R.id.txtPass);
        contra2 = view.findViewById(R.id.txtPassConf);
        textLogin = view.findViewById(R.id.btnLogin);
        textRegistrar = view.findViewById(R.id.btnRegistro);
        spinnerCarrera = view.findViewById(R.id.carreras);
        btnVerPass = view.findViewById(R.id.btnVerPass);



        btnVerPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verPass) {
                    contra1.setTransformationMethod(new PasswordTransformationMethod());
                    btnVerPass.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    verPass = false;
//                    createSimpleNotification();
                } else {
                    contra1.setTransformationMethod(null);
                    btnVerPass.setImageResource(R.drawable.ojoscruzados);
                    verPass = true;

                }
            }
        });
        //Evento clic para el botón "Ingresar" por si ya se tiene una cuenta
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragmentLogin = new LoginFragment(); //Se asigna el fragment que se abrirá
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragmentLogin);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        //Una vez llenado el registro, se envía el correo al hacer clic en este botón
        textRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginFields()) {
                    String usuario = user.getText().toString();
                    String nombres = nombre.getText().toString();
                    String apellidos = apellido.getText().toString();
                    String correo = email.getText().toString();
                    String pass1 = contra1.getText().toString();
                    String pass2 = contra2.getText().toString();
                    int carrera = spinnerCarrera.getSelectedItemPosition() + 1;
                    registro(usuario, nombres, apellidos, pass1, pass2, correo, carrera);
//                    createSimpleNotification();
                }
            }
        });
//        createChannel();
        String url = "https://www.api.katiosca.com/carreras/";
        RequestQueue requestQueue;
        List<String> carreraLista = new ArrayList<>();

        // Create Volley request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        // Make request to server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String carrera = jsonObject.getString("nombre_carrera");
                            carreraLista.add(carrera);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, carreraLista);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerCarrera.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorObject = new JSONObject(errorMessage);
                            String errorText = errorObject.getString("error");
                            Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        // Add request to queue
        requestQueue.add(request);


        return view;
    }
    public boolean validateLoginFields() {
        String username = user.getText().toString().trim();
        String password1 = contra1.getText().toString().trim();
        String password2 = contra2.getText().toString().trim();
        String nombreuser = nombre.getText().toString().trim();
        String apellidouser = apellido.getText().toString().trim();
        String correo = email.getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            user.setError("Ingrese su número de cuenta");
            return false;
        }
        if (TextUtils.isEmpty(password1)) {
            contra1.setError("Ingrese una contraseña");
            return false;
        }
        if (TextUtils.isEmpty(password2)) {
            contra2.setError("Ingrese la confrimación de la contraseña");
            return false;
        }
        if (TextUtils.isEmpty(nombreuser)) {
            nombre.setError("Ingrese su nombre completo");
            return false;
        }
        if (TextUtils.isEmpty(apellidouser)) {
            apellido.setError("Ingrese su apellido completo");
            return false;
        }
        if (TextUtils.isEmpty(correo)) {
           email.setError("Ingrese su correo");
            return false;
        }
        if (!password1.equals(password2)) {
           contra1.setError("Las contraseñas no coinciden");
           contra2.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    public boolean registro(String username, String nombre, String apellido, String password1,
                            String password2, String email, int carrera) {
        String url = "https://www.api.katiosca.com/auth/registro/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        System.out.println(carrera);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", nombre);
            jsonObject.put("last_name", apellido);
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("carrera", carrera);
            jsonObject.put("password1", password1);
            jsonObject.put("password2", password2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    String authToken = extractAuthToken(response);
                    TokenManager tokenManager = new TokenManager(getContext());
                    tokenManager.saveAuthToken(authToken);
                    LoginFragment fragmentCorreo = new LoginFragment(); //Se asigna el fragment que se abrirá
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragmentCorreo);
                    transaction.addToBackStack(null);
                    transaction.commit();
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorObject = new JSONObject(errorMessage);
                            String errorText = errorObject.getString("error");
                            System.out.println(errorText);
                            Toast.makeText(getContext(), "Error en la creacion del usuario", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
        return false;
    }

    private String extractAuthToken(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");
            return data.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
//    private void createChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    MY_CHANNEL_ID,
//                    "MySuperChannel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setDescription("SUSCRIBETE");
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

//    private void createSimpleNotification() {
//        Intent intent = new Intent(getActivity(),LoginMainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getContext(), MY_CHANNEL_ID)
//                        .setSmallIcon(R.drawable.uth_share_logo)
//                        .setContentTitle("UTH-Share")
//                        .setContentText("Registro")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("Se te ha enviado un email de verificación, por favor revisa tu correo para culminar con el registro"))
//
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setContentIntent(pendingIntent) // Agrega aquí el PendingIntent
//                        .setAutoCancel(true);;
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build()
//        );
//
//    }
}
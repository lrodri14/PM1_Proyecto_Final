package com.example.myapplication.ui.perfiluser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.ViewActivity;
import com.example.myapplication.utilities.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditPerfilUser extends Fragment {

    ImageView btnCambiarPass, btn_imagen,images, btnVolver, foto, btn_editar;
    static final int Result_galeria = 101;

    static final  int REQUEST_IMAGE = 102;
    static final  int PETICION_ACCESS_CAM = 201;

    Bitmap bitmap;

    String currentPhotoPath;
    EditText usuario,nombre,apellido, correo;

    TokenManager tokenManager;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editperfiluser, container, false);
        tokenManager = new TokenManager(getContext());
        nombre = view.findViewById(R.id.txtnombre);
        apellido = view.findViewById(R.id.txtapellido);
        correo = view.findViewById(R.id.txtcorreo);
        btnCambiarPass = view.findViewById(R.id.btnCambiarPass);
        btn_imagen = view.findViewById(R.id.btn_imagen);
        btnVolver = view.findViewById(R.id.btnVolverMenu);
        images = view.findViewById(R.id.images);
        btn_editar = view.findViewById(R.id.btn_editar);
        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "CambioContrasenaFragment";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });

        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "CambioContrasenaFragment";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "perfiluser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });

        btn_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = nombre.getText().toString();
                String apellidos = apellido.getText().toString();
                String correos =correo.getText().toString();
                registro( nombres, apellidos,correos);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/personal";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("usuario");
                        nombre.setText(user.getString("first_name"));
                        apellido.setText(user.getString("last_name"));
                        correo.setText(user.getString("email"));


                        if (data.getString("foto_de_perfil") != "null"){
                            Picasso.get().load("https://www.api.katiosca.com" + data.getString("foto_de_perfil")).into(foto);
                        }

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
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        getActivity().getMenuInflater().inflate(R.menu.fotos_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_camara:
                       permisos();
                        return true;
                    case R.id.menu_galeria:
                        GaleriaImagenes();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Result_galeria && data != null) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                    images.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (requestCode == REQUEST_IMAGE) {
                try {
                    File foto = new File(currentPhotoPath);
                    bitmap = BitmapFactory.decodeFile(foto.getAbsolutePath());
                    images.setImageURI(Uri.fromFile(foto));
                } catch (Exception ex) {
                    ex.toString();
                }
            }
        }
    }


    private void GaleriaImagenes()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Result_galeria);
    }


    private void permisos() {
        // Verificar si el permiso de cámara ya está concedido
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permiso no concedido, solicitarlo al usuario
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, PETICION_ACCESS_CAM);
        } else {
            // Permiso concedido, hacer algo aquí...
            dispatchTakePictureIntent();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PETICION_ACCESS_CAM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(requireContext(), "Se necesita el permiso de la cámara", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }
    public boolean registro(String nombres, String apellidos,String email) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/personal";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("usuario");
                        nombre.setText(user.getString("first_name"));
                        apellido.setText(user.getString("last_name"));
                        correo.setText(user.getString("email"));
                        String nombreFragment = "perfiluser";
                        Intent intent = new Intent(getContext(), ViewActivity.class);
                        intent.putExtra("nombreFragment", nombreFragment);
                        startActivity(intent);
                        Toast toast = Toast.makeText(getContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT);
                        toast.show();
                        // Actualizar otros campos
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

            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("first_name", nombres);
                    jsonBody.put("last_name", apellidos);
                    jsonBody.put("email", email);
                    // Agregar otros campos
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        queue.add(request);

        return false;
    }



}


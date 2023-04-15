package com.example.myapplication.ui.perfiluser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ConversacionActivity;
import com.example.myapplication.R;
import com.example.myapplication.ViewActivity;
import com.example.myapplication.utilities.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditPerfilUser extends Fragment {

    ImageView btnCambiarPass, btn_imagen,images, btnVolver, foto, btn_editar;
    static final int Result_galeria = 101;

    static final  int REQUEST_IMAGE = 102;
    static final  int PETICION_ACCESS_CAM = 201;

    Bitmap bitmap;

    String currentPhotoPath;
    EditText usuario,nombre,apellido, correo;

    TokenManager tokenManager;

    private class UploadFileTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... objects) {
            String nombre = (String) objects[0];
            String apellido = (String) objects[1];
            String correo = (String) objects[2];
            Bitmap bitmap = (Bitmap) objects[3];
            try {
                editarPerfil(nombre, apellido, correo, bitmap);
                String nombreFragment = "perfiluser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editperfiluser, container, false);
        tokenManager = new TokenManager(getContext());
        nombre = view.findViewById(R.id.txtnombre);
        apellido = view.findViewById(R.id.txtapellido);
        correo = view.findViewById(R.id.txtcorreo);
//        btnCambiarPass = view.findViewById(R.id.btnCambiarPass);
//        btn_imagen = view.findViewById(R.id.btn_imagen);
        btnVolver = view.findViewById(R.id.btnVolverMenu);
        images = view.findViewById(R.id.images);

        btn_editar = view.findViewById(R.id.btn_editar);
//        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nombreFragment = "CambioContrasenaFragment";
//                Intent intent = new Intent(getContext(), ViewActivity.class);
//                intent.putExtra("nombreFragment", nombreFragment);
//                startActivity(intent);
//            }
//        });

//        btnCambiarPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nombreFragment = "CambioContrasenaFragment";
//                Intent intent = new Intent(getContext(), ViewActivity.class);
//                intent.putExtra("nombreFragment", nombreFragment);
//                startActivity(intent);
//            }
//        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFragment = "perfiluser";
                Intent intent = new Intent(getContext(), ViewActivity.class);
                intent.putExtra("nombreFragment", nombreFragment);
                startActivity(intent);
            }
        });

//        btn_imagen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(view);
//            }
//        });
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = nombre.getText().toString();
                String apellidos = apellido.getText().toString();
                String correos =correo.getText().toString();
                new UploadFileTask().execute(nombres, apellidos, correos, bitmap);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.katiosca.com/perfiles/personal";
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("usuario");
                        nombre.setText(user.getString("first_name"));
                        apellido.setText(user.getString("last_name"));
                        correo.setText(user.getString("email"));
                        if (data.getString("foto_de_perfil") != "null"){
                            Picasso.get().load("https://www.api.katiosca.com" + data.getString("foto_de_perfil")).into(images);
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

    private void editarPerfil(String nombre, String apellido, String correo, Bitmap bitmap) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        // create a MultipartBody.Builder to build the request body
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("first_name", nombre)
                .addFormDataPart("last_name", apellido)
                .addFormDataPart("email", correo);

        // add the image file to the request body
        if (bitmap != null) {
            // convert the Bitmap to a byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // add the byte array as a file to the request body
            requestBodyBuilder.addFormDataPart("foto_de_perfil", "imagen.jpg",
                    RequestBody.create(MediaType.parse("image/jpeg"), byteArray));
        }

        RequestBody requestBody = requestBodyBuilder.build();

        // create the request object
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://www.api.katiosca.com/perfiles/personal")
                .header("Authorization", "Token " + tokenManager.getAuthToken())
                .patch(requestBody)
                .build();

        // execute the request and handle the response
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            // handle the response data here
        } else {
            // handle the error here
        }
    }
}


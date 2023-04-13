package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.utilities.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversacionActivity extends AppCompatActivity {

    private ListView mChatListView;
    private ArrayList<String> mChatMessages;
    private ArrayAdapter<String> mChatListAdapter;
    int grupoId;

    TokenManager tokenManager;


    private static final int REQUEST_SELECT_IMAGE = 1;
    private static final int REQUEST_RECORD_AUDIO = 2;
    private static final int REQUEST_RECORD_VIDEO = 3;
    private static final int REQUEST_SELECT_FILE = 4;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 5;
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 6;
    private static final int REQUEST_PERMISSION_CAMERA = 7;

    //Variables de media-Attachment
    private File selectedFile;
    private Uri selectedImageUri;
    private Uri selectedAudioUri;
    private Uri selectedVideoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        tokenManager = new TokenManager(this);
        grupoId = getIntent().getIntExtra("chatGroupId", -1);

        ActionBar actionBar = getSupportActionBar();
        ImageView puntos = new ImageView(this);
        puntos.setImageResource(R.drawable.trespuntos);

// Establecemos el ImageButton como vista personalizada de la ActionBar
        actionBar.setCustomView(puntos, new ActionBar.LayoutParams(Gravity.END));

// Habilitamos la vista personalizada de la ActionBar
        actionBar.setDisplayShowCustomEnabled(true);
// Agregamos un listener de clic al ImageButton
        puntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });



        // Obtiene el nombre del grupo de chat seleccionado de la actividad anterior
        Intent intent = getIntent();
        String chatGroupName = intent.getStringExtra("chatGroupName");

        // Establece el título de la actividad como el nombre del grupo de chat seleccionado
        getSupportActionBar().setTitle(chatGroupName);

       extraerMensajes(grupoId);

        // ----Agrega un mensaje cuando se presiona el botón de enviar-----
        final EditText messageEditText = findViewById(R.id.new_message_edittext);
        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                if (!message.isEmpty()) {
                    CrearMensaje(message, grupoId);
                    messageEditText.setText("");
                }
            }
        });

        // Vincula el método showAttachmentOptions con el botón de adjuntar
        findViewById(R.id.attach_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttachmentOptions(v);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            // almacenar imagen
            selectedImageUri = data.getData();
          //  ImageView imageView = findViewById(R.id.attach_imageview);
         //   imageView.setImageURI(selectedImageUri);

        }

        if (requestCode == REQUEST_RECORD_AUDIO && resultCode == RESULT_OK && data != null) {
            //Almacenar el video
            selectedAudioUri = data.getData();
            MediaPlayer mediaPlayer = MediaPlayer.create(this, selectedAudioUri);
            mediaPlayer.start();

            // Usa el audio grabado aquí
        }

        if (requestCode == REQUEST_RECORD_VIDEO && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            // VideoView videoView = findViewById(R.id.attach_videoview);
            // videoView.setVideoURI(videoUri);
            // videoView.start();
            // Usa el video grabado aquí

            // Guardar el video en el almacenamiento local
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DISPLAY_NAME, "video.mp4");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            Uri videoUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            try {
                OutputStream os = resolver.openOutputStream(videoUri);
                InputStream is = resolver.openInputStream(selectedVideoUri);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
                os.close();
                is.close();
                // Usa la Uri del video guardado aquí
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == REQUEST_SELECT_FILE && resultCode == RESULT_OK) {
            // Obtiene el archivo URI y el PATH

            //Almacenar el archivo
            Uri uri = data.getData();
            String path = uri.getPath();
            DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
            String originalFileName = documentFile.getName();


            try {
                // Obtener el contenido del archivo como un InputStream
                InputStream inputStream = getContentResolver().openInputStream(uri);

                // Crear un archivo local donde se almacenará el contenido del archivo
                selectedFile = new File(getFilesDir(), originalFileName);

                // Escribir el contenido del archivo en el archivo local
                FileOutputStream outputStream = new FileOutputStream(selectedFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                // Actualizar la lista de elementos que se muestran en el ListView
                //
                mChatMessages.add(originalFileName);

                mChatListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mChatMessages);
                //------ Configura el ListView----------------
                mChatListView = findViewById(R.id.message_listview);
                mChatListView.setAdapter(mChatListAdapter);
                mChatListAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


//Metodo para seleccionar la imagen desde la galeria
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    //Metodo para grabar Audio
    private void recordAudio() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, REQUEST_RECORD_AUDIO);
    }

    //Metodo para grabar Video
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_RECORD_VIDEO);
    }



    //Metodo para mostrar las opciones y llamar sus metodos funcionales
    public void showAttachmentOptions(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.attachment_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_image:
                        // Verificar si se ha concedido permiso para acceder a la galería
                        if (ActivityCompat.checkSelfPermission(ConversacionActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Si el permiso no se ha concedido, solicitar al usuario que lo conceda
                            ActivityCompat.requestPermissions(ConversacionActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                        } else {
                            // Si el permiso se ha concedido, iniciar la actividad para seleccionar una imagen de la galería
                            selectImageFromGallery();
                        }
                        return true;
                    case R.id.menu_document:
                        // Verificar si se ha concedido permiso para acceder a los documentos
                        if (ActivityCompat.checkSelfPermission(ConversacionActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Si el permiso no se ha concedido, solicitar al usuario que lo conceda
                            ActivityCompat.requestPermissions(ConversacionActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                        } else {
                            // Si el permiso se ha concedido, iniciar la actividad para seleccionar un archivo de los documentos
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*"); // Permitir seleccionar cualquier tipo de archivo
                            startActivityForResult(intent, 4);
                        }
                        return true;
                    case R.id.menu_audio:
                        // Verificar si se ha concedido permiso para grabar audio
                        if (ActivityCompat.checkSelfPermission(ConversacionActivity.this, android.Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Si el permiso no se ha concedido, solicitar al usuario que lo conceda
                            ActivityCompat.requestPermissions(ConversacionActivity.this,
                                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                                    REQUEST_PERMISSION_RECORD_AUDIO);
                        } else {
                            // Si el permiso se ha concedido, iniciar la actividad para grabar audio
                            recordAudio();
                        }
                        return true;
                    case R.id.menu_video:
                        // Verificar si se ha concedido permiso para acceder a la cámara
                        if (ActivityCompat.checkSelfPermission(ConversacionActivity.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Si el permiso no se ha concedido, solicitar al usuario que lo conceda
                            ActivityCompat.requestPermissions(ConversacionActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_PERMISSION_CAMERA);
                        } else {
                            // Si el permiso se ha concedido, iniciar la actividad para grabar un video
                            recordVideo();
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.show(); // muestra el menú emergente
    }


    private void showPopupMenu(View view) {
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(this, view);
        this.getMenuInflater().inflate(R.menu.menu_opciones_grupos, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_archivos:

                        return true;
                    case R.id.menu_descripcion:

                        String nombreFragment = "DescripcionGrupoFragment";
                        Intent intent = new Intent(ConversacionActivity.this, ViewActivity.class);
                        intent.putExtra("nombreFragment", nombreFragment);
                        startActivity(intent);


                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    public void extraerMensajes(int grupoId){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.katiosca.com/chats/" + grupoId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        mChatMessages = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject usuario = jsonArray.getJSONObject(i).getJSONObject("usuario");
                            String mensaje = data.getString("mensaje");
                            String nombreCompleto = usuario.getString("first_name") + " " + usuario.getString("last_name");
                            mChatMessages.add(nombreCompleto + ": " + mensaje);
                        }
                        // Inicializa el adaptador de la lista de mensajes del chat
                        mChatListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mChatMessages);
                        //------ Configura el ListView----------------
                        mChatListView = findViewById(R.id.message_listview);
                        mChatListView.setAdapter(mChatListAdapter);
                        mChatListAdapter.notifyDataSetChanged();
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
    }

    public void CrearMensaje(String mensaje, int grupoId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.api.katiosca.com/chats/" + grupoId;
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("mensaje", mensaje);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    extraerMensajes(grupoId);
                },
                error -> {
                    //
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + tokenManager.getAuthToken());
                return headers;
            }
        };

        queue.add(request);
    }

}

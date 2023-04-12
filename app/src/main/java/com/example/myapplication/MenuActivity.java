package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.perfiluser.perfiluser;
import com.example.myapplication.utilities.TokenManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMenuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private FloatingActionButton btnCrearGrupo;

    TextView nombre, correo;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nombre = (TextView) findViewById(R.id.idUsuario);
        correo = (TextView) findViewById(R.id.usuarioCorreo);
        tokenManager = new TokenManager(this);

        btnCrearGrupo = (FloatingActionButton)findViewById(R.id.fab);

        // Carga el fragmento que contiene la lista de chats
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListaChatFragment listChatFragment = new ListaChatFragment();
        fragmentTransaction.add(R.id.fragment_container, listChatFragment);
        fragmentTransaction.commit();

        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CrearGrupoFragment crearGrupoFragment = new CrearGrupoFragment();
                crearGrupoFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
                crearGrupoFragment.show(fragmentManager, "crear_grupo_dialog");
            }
        });
        ActionBar actionBar = getSupportActionBar();
// Establecemos el icono en la ActionBar
        actionBar.setIcon(R.drawable.logo_uth);
        actionBar.setDisplayShowHomeEnabled(true);





        /*setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.katiosca.com/perfiles/personal";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject usuario = data.getJSONObject("usuario");
                        nombre.setText(usuario.getString("first_name") + " " + usuario.getString("last_name"));
                        correo.setText(usuario.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
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

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment miFragment=null;
        boolean fragmentSeleccionado=false;


        if (id == R.id.nav_chats) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {

            String nombreFragment = "perfiluser";
            Intent intent = new Intent(MenuActivity.this, ViewActivity.class);
            intent.putExtra("nombreFragment", nombreFragment);
            startActivity(intent);

            //miFragment= new perfiluser();
            //fragmentSeleccionado=true;
        } else if (id == R.id.nav_lista) {
            miFragment= new ListaAmigosFragment();
            fragmentSeleccionado=true;
        } else if (id == R.id.nav_buscar) {
            miFragment= new ListaUsuariosFragment();
            fragmentSeleccionado=true;
        } else if (id == R.id.nav_noti) {
            miFragment= new NotificacionFragment();
            fragmentSeleccionado=true;
    }else if (id == R.id.salir) {
            Intent intent = new Intent(this, LoginMainActivity.class);
            startActivity(intent);
        }

        if (fragmentSeleccionado==true){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,miFragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
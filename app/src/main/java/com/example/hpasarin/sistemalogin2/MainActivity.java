package com.example.hpasarin.sistemalogin2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences prefs;
    //cargar configuración aplicación Android usando SharedPreferences
    public void cargarConfiguracion()
    {
        //si hay fichero de configuracion, accedo a sus recursos.
        //prefs = getSharedPreferences("configaplicacion", Context.MODE_APPEND);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);

        //devolvera 99 si no encuentra ningun par key-value para id seria el valor por defecto
        Log.d("PRUEBA","TENGO ESTAS PREFERENCIAS"+prefs.getAll().toString());
        if (prefs.getString("usuarioActual","99").equals("99")){
            Toast.makeText(getApplicationContext(),"Debe logearse para acceder a geolocalización.",Toast.LENGTH_SHORT).show();
        }

        this.setTitle("identificado como "+(prefs.getString("usuarioActual", "99")));
        //opUbicacionFichero.setChecked(prefs.getBoolean("GuardarSDCard", true));
    }


    @Override
    protected void onResume() {
        super.onResume();
        //buen sitio para cargar preferencias.
        cargarConfiguracion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        cargarConfiguracion();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* se comenta para que no aparezca elboton flotante
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //para resolver los clicks menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //gestionamos los clicks del menu de la actionbar
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //arranca activity de preferencias:
            startActivity(new Intent(this,ActividadPreferencias.class));

        }else if (id==R.id.action_salir){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        boolean fragmentTransaction= false;


        if (id == R.id.nav_camera) {
            // Handle the camera action
          fragment= new Login();
            fragmentTransaction = true;
        } else if (id == R.id.nav_gallery) {
            fragment= new FragmentObtenerLista();
            fragmentTransaction = true;
        } else if (id == R.id.nav_IMEI) {
            fragment= new ObtenerImei();
            fragmentTransaction = true;
        }  else if (id == R.id.nav_send) {
            return true;
        }  else if (id == R.id.nav_preferencias) {
            //startActivity(new Intent(this,Preferencias.class));
            //arranco una activiti en vez de hacer una transaccion, porque
            //el fragment está dentro de la clase ActividadPreferencias, no es
            //independiente
            startActivity(new Intent(this,ActividadPreferencias.class));
        }  else if (id == R.id.nav_preferenciasPorDefecto) {

            //PONGO COMO TIPO DE MAPA EL QUE QUIERA PONER POR DEFECTO
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            prefs.getString("opcionTipoMapa","SAT");
            Snackbar.make(this.findViewById(R.id.contenedor), "Se restablecen las opciones por defecto", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }

        if (fragmentTransaction){
            getSupportFragmentManager().beginTransaction()
                    //para que no queden memorizadas y al dar boton atras vayan a ellas
                    .addToBackStack(null)
                    .replace(R.id.contenedor,fragment)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }





}

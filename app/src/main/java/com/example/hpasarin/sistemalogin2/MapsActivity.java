package com.example.hpasarin.sistemalogin2;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import static com.example.hpasarin.sistemalogin2.R.id.map;

//OnMapReadyCallback sirve para referenciar el mapa justo cuando esté listo.

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    final int MARKER_UPDATE_INTERVAL = 3000; /* milliseconds */

    //handler para comunicarnos con el hilo principal donde esta LA UI
    Handler handler = new Handler();
    JSONArray arrayJSON;
    Marker marker;
    Location location;
    private GoogleMap mapa;
    Random alea;
    Thread hilo;
    LocationManager manager;
    static String datosObtenidos;
    static URL url=null;
    long tiempo = 8000; // 5 segundos
    float distancia = 10; // 10 metros
    CameraUpdate cameraUpdate;
    TareaPesada tarea;
    JSONObject objetoJSON;
    ArrayList<LatLng> arrayPuntosRuta;
    CameraUpdate actualizacionCamara;
    CameraPosition cameraPosition;



    Runnable updateMarker = new Runnable() {
        @Override
        public void run() {

            //el id del vehiculo que queremos consultar lo recibimos mediante los extras del
            //intent que llamó a esta actividad.

            String queVehiculo=getIntent().getStringExtra("idVehiculo");
            String datos= obtenerDatos(queVehiculo);
            Log.d("prueba",datos);

            if (datosObtenidos!=null){

                // Toast.makeText(getApplicationContext(),datosObtenidos,Toast.LENGTH_SHORT).show();

                try {
                    arrayJSON = new JSONArray(datosObtenidos);
                    arrayPuntosRuta = new ArrayList<LatLng>();

                    arrayPuntosRuta.clear(); //necesario para refrescar los markers de la ruta


                    //recorro el arrayJSON para recoger los datos y añadir cada par (latitud-longitud) al array de puntos que
                    //conformarán la ruta.

                    for (int i=0;i<arrayJSON.length();i++){
                        objetoJSON = arrayJSON.getJSONObject(i);
                        marker = mapa.addMarker(new MarkerOptions().position(new LatLng(objetoJSON.getDouble("lat"),objetoJSON.getDouble("lon") )));

                        //añado el punto a la ruta:
                        arrayPuntosRuta.add(new LatLng(objetoJSON.getDouble("lat"),objetoJSON.getDouble("lon") ));
                    }


                    //llevo la camara a la posición mas actual. Primero creo un objeto
                    //cameraPosition, que luego se lo paso al metodo animateCamera

                    cameraPosition = new CameraPosition.Builder()
                            .target(arrayPuntosRuta.get(0))
                            .zoom(20)
                            .bearing(300)
                            .tilt(30)
                            .build();

                    mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    //una vez tengo todos los puntos en el arrayPuntosRuta dibujo la polilinea
                    Polyline ruta = mapa.addPolyline(
                            new PolylineOptions()
                                    .addAll(arrayPuntosRuta)
                                    .color(Color.BLACK)
                                    .width(8)
                    );

                    //CENTRO EL MAPA EN LA ULTIMA POSICION DEL ARRAY //
                    if  (arrayPuntosRuta.size()>0) {

                        Log.d("prueba","el ultimo punto es: "+arrayPuntosRuta.size());
                        //el último parámetro es el zoom, 1 es lejano 20 cercano
                        actualizacionCamara =CameraUpdateFactory.newLatLngZoom(arrayPuntosRuta.get(0),16);
                        mapa.animateCamera(actualizacionCamara);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }



            handler.postDelayed(this, MARKER_UPDATE_INTERVAL);

        }
    };


//obtiene los marcadores del vehiculo que se le pasa como parametro
    public String obtenerDatos(String idVehiculo){

        tarea = new TareaPesada(MapsActivity.this);
        tarea.execute(idVehiculo);

//al terminar la ejecucion de la tarea, tendremos algo en datosObtenidos
        return datosObtenidos;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        datosObtenidos ="";
        alea = new Random();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(map);

        mapFragment.getMapAsync(MapsActivity.this);

        //ejecuta el metodo updateMarker, a intervalo definido en el segundo parametro
        handler.postDelayed(updateMarker, MARKER_UPDATE_INTERVAL);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

//este hilo no esta haciendo nada hasta que no se descomente //hilo.run();
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    actualizarMarcador();

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }//fin while
            }

            private void actualizarMarcador() {
                Log.d("prueba", "marcador actualizado");
                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(map);

                GoogleMap miMapa = mapa;


                miMapa.clear();

                miMapa.addMarker(new MarkerOptions()
                        .position(new LatLng(alea.nextInt(4) + 41, -(alea.nextInt(1) + 5)))
                        .title("Hello world"));


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;

        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.setMinZoomPreference(5);


//centro mapa en asturias
        //  cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.3,-5.2), 3);
        //mapa.animateCamera(cameraUpdate);

    }



    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateMarker);

        super.onDestroy();
    }
}


class TareaPesada extends AsyncTask<String,String,String> {
    String linea = "";
    StringBuffer buffer = null;
    Activity actividadUI;
    OutputStream os = null;
    BufferedWriter bw = null;
    String vehiculoConsulta = "";
    String data = "";
    HttpURLConnection conn;

    public TareaPesada(MapsActivity mainActivity) {
        this.actividadUI = mainActivity;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);


    }

    @Override
    protected String doInBackground(String... params) {

        try {
            vehiculoConsulta = params[0];
            URL ruta = new URL("http://www.motosmieres.com/muestraJSON.php");
            conn = (HttpURLConnection) ruta.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            //primero envio el idVehiculo que recibi en el intent

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);


            //configuro la conexion para enviar parametros
            //en el php del servidor, debe estar tambien en POST
            conn.setRequestMethod("POST");


            os = conn.getOutputStream();
                 bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            data = URLEncoder.encode("a", "UTF-8") + "=" + URLEncoder.encode(vehiculoConsulta, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        /////luego puedo recibir los datos que me devuelve

        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader br = new BufferedReader(isr);

        buffer = new StringBuffer();

        linea = "";


        try {
            while (((linea = br.readLine()) != null)) {

                buffer.append(linea);

            }


            is.close();
            isr.close();
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        conn.disconnect();

        publishProgress(buffer.toString());
        return buffer.toString();
    }




    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("pruebas","se recibio:"+s);
//ejecuta el metodo updateMarker, a intervalo definido en el segundo parametro
        //handler.postDelayed(updateMarker, MARKER_UPDATE_INTERVAL);

        //de esta manera podemos pasar datos a la activity principal
        MapsActivity.datosObtenidos=s;
    }
}

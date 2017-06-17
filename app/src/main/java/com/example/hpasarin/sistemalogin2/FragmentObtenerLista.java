package com.example.hpasarin.sistemalogin2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentObtenerLista#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentObtenerLista extends Fragment {

    public static ArrayList<Vehiculo> vehiculos;
    Tarea tarea;
    URL url;
    String ruta;
    ListView lista;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentObtenerLista() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentObtenerLista.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentObtenerLista newInstance(String param1, String param2) {
        FragmentObtenerLista fragment = new FragmentObtenerLista();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_fragment_obtener_lista, container, false);
        lista = (ListView) vista.findViewById(R.id.vistaLista);
        //conectarse asyncronamente a la url

      //solo pude arrancar la activity del mapa desde aqui en el onCreateView

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                intent.putExtra("idVehiculo",vehiculos.get(position).idVehiculo);
                startActivity(intent);
            }
        });

        ruta= "http://www.motosmieres.com/mostrarVehiculos.php";


        tarea = new Tarea((MainActivity) getActivity());

        //el primer parametro es la direccion del archivo php que devolvera los vehiculos
        //del gestor con el nombre  del usuario que está logueado como segundo parametro.

        //si hay fichero de configuracion, accedo a sus recursos. para obtener el segundo parametro
        //SharedPreferences prefs = getActivity().getSharedPreferences("configaplicacion", Context.MODE_APPEND);
        SharedPreferences prefes = ((MainActivity) getActivity()).prefs;
        tarea.execute(ruta,prefes.getString("usuarioActual","1")); //si no hay id en las prefs, id=3
        return vista;
    }

}

class Tarea extends AsyncTask<String,Void,String> {
    HttpURLConnection con;
    String data="";
    String idGestor="1";
    Activity actividadUI;
    ArrayList<String> vehiculos=null;
    JSONArray arrayJSON;
    OutputStream os = null;
    JSONObject linea;
    BufferedWriter bw = null;
    InputStreamReader bis;
    StringBuffer result=null;
    BufferedReader lectorEntrada=null;
    String line="";
    URL url;

    public Tarea(MainActivity mainActivity) {
        this.actividadUI= mainActivity;
    }
    ArrayList<Vehiculo> vehiculos_data;
    @Override

    protected String doInBackground(String... params) {

        //obtenemos el primer parametro, que
        //es un String con la ruta
        //para poder usarlo con HttpURLConnection, lo pasamos a URL
        //y el segundo parametro que recibimos lo guardamos en idGestor
        //para luego hacer la consulta y obtener los vehiculos solo de
        //ese gestor.

        idGestor= params[1];
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            con = (HttpURLConnection) url.openConnection();




        } catch (IOException e) {
            e.printStackTrace();
        }
        con.setReadTimeout(10000);
        con.setConnectTimeout(15000);
        con.setDoOutput(true);
        con.setDoInput(true);
        try {
            //en el php del servidor, debe estar tambien en POST
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }





        try {
            os = con.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            data = URLEncoder.encode("a", "UTF-8") + "=" + URLEncoder.encode(idGestor, "UTF-8") ;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {

            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

/*
                Parsear el flujo con formato JSON a una lista de Strings
                que permitan crean un adaptador
                 */
            bis = new InputStreamReader(con.getInputStream());
            lectorEntrada = new BufferedReader(bis);



            //mientras el lectorEntrada encuentre algo lo añado ;
            result= new StringBuffer();
            while ((line = lectorEntrada.readLine()) != null) {
                result.append(line);
            }



            con.getInputStream().close();
            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("RESULTADO", result.toString());
        return result.toString();



    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            arrayJSON = new JSONArray(s);



            vehiculos_data = new ArrayList<>();

            for (int i=0;i<arrayJSON.length();i++){
                linea =arrayJSON.getJSONObject(i);
                Log.d("RESULTADO", "Se recogio correctamente id_vehiculo: " + linea.get("id_vehiculo"));
                //    vehiculos.add(linea.getString("id_vehiculo"));
                Random genAlea = new Random();
                switch (linea.getInt("icono")){
                    case 1:
                        vehiculos_data.add(new Vehiculo(R.drawable.busnegro,linea.getString("nombre_linea"),linea.getString("tipo"),linea.getString("id_vehiculo")));
                        break;
                    case 2:
                        vehiculos_data.add(new Vehiculo(R.drawable.busblanco,linea.getString("nombre_linea"),linea.getString("tipo"),linea.getString("id_vehiculo")));
                        break;
                    case 3:
                        vehiculos_data.add(new Vehiculo(R.drawable.busazul,linea.getString("nombre_linea"),linea.getString("tipo"),linea.getString("id_vehiculo")));
                        break;
                    default:
                        vehiculos_data.add(new Vehiculo(R.drawable.busamarillo,linea.getString("nombre_linea"),linea.getString("tipo"),linea.getString("id_vehiculo")));
                        break;

                }




            }

            Log.d("RESULTADO", "Se recogio correctamente el JSON, AHORA A CARGAR LA LISTVIEW");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView lista = (ListView) actividadUI.findViewById(R.id.vistaLista);

                /* Se crea un adaptador con el el resultado del parsing
        que se realizó al arreglo JSON
         */




        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.actividadUI.getBaseContext(),android.R.layout.simple_list_item_1,vehiculos);

        AdaptadorVehiculo adapter = new AdaptadorVehiculo(actividadUI,R.layout.layout_fila, vehiculos_data);
        // Relacionar adaptador a la lista
        lista.setAdapter(adapter);

        FragmentObtenerLista.vehiculos = vehiculos_data;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

/*
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    */
}

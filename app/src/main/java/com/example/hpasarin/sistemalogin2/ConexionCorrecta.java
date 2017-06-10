package com.example.hpasarin.sistemalogin2;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConexionCorrecta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConexionCorrecta extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
     static final String ARG_PARAM1 = "param1";
     static final String ARG_PARAM2 = "param2";
    SharedPreferences prefs;
    // TODO: Rename and change types of parameters
    private static String Usuario;
    private static String Mensaje;
    static Bundle args;
    TextView tvUsuarioLogeado;

    public ConexionCorrecta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConexionCorrecta.
     */
    // TODO: Rename and change types and number of parameters
    public static ConexionCorrecta newInstance(String param1, String param2) {
        ConexionCorrecta fragment = new ConexionCorrecta();

        Log.d("PRUEBA","SE crea ConexionCorrecta (el fragment)");

         args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);


        Log.d("PRUEBA","Usuario toma el valor: "+ args.getString(ARG_PARAM1));

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {//si se restablece estado?
            this.Usuario = getArguments().getString(ARG_PARAM1);
            this.Mensaje = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("PRUEBA","SE INSTANCIA ConexionCorrecta (el fragment)");
        //ahora que tengo los textview en pantalla, ya puedo trabajar con ellos
        View vista = inflater.inflate(R.layout.fragment_conexion_correcta,container,false);
        TextView tvUsuariocorrecto = (TextView) this.getActivity().findViewById(R.id.tvUser);
        TextView mensaje = (TextView) this.getActivity().findViewById(R.id.tvPass);

            if (!Mensaje.equals("Identificado correctamente")) {
                tvUsuariocorrecto.setText("Contraseña o usuario erróneos");

            }else
        tvUsuariocorrecto.setText("bienvenido "+this.Usuario);
        mensaje.setText(Mensaje);
        tvUsuariocorrecto = (TextView) getActivity().findViewById(R.id.tvLogeadoComo);
        guardarUser();


        tvUsuarioLogeado = (TextView) getActivity().findViewById(R.id.tvLogeadoComo);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conexion_correcta, container, false);
    }

    public void guardarUser(){
        //si hay fichero de configuracion, accedo a sus recursos.
         prefs = getActivity().getSharedPreferences("configaplicacion", Context.MODE_APPEND);
        SharedPreferences.Editor editorDePreferencias = prefs.edit();
        editorDePreferencias.putString("id",this.Usuario);
        editorDePreferencias.commit();

        Log.d("PRUEBA","SE GUARDA EL ID EN PREFERENCIAS: "+this.Usuario);
        Log.d("PRUEBA","preferencias: "+getActivity().getPreferences(Context.MODE_APPEND).getAll().toString());

        //devolvera 99 si no encuentra ningun par key-value para id seria el valor por defecto
        getActivity().setTitle("identificado como "+(prefs.getString("id", "99")));
        //opUbicacionFichero.setChecked(prefs.getBoolean("GuardarSDCard", true));

        //se pone el nombre de la persona logeada en la nav-header_main
        tvUsuarioLogeado.setText(prefs.getString("id","haga login para ver sus vehiculos"));
    }

}

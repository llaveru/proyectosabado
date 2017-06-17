package com.example.hpasarin.sistemalogin2;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ObtenerImei extends Fragment {


    private TelephonyManager tMgr;
    private String idTelefono;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recojo el String idTelefono
        obtenerIdTelefono();
    }

    public ObtenerImei() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflo tel layout del fragment para poder obtener las views y darles valores
        View vista =inflater.inflate(R.layout.fragment_obtener_imei, container, false);

        TextView casillaIMEI = (TextView) vista.findViewById(R.id.tvIMEI);
        casillaIMEI.setText(idTelefono);



        return vista;
    }


    //método para obtener la id del teléfono:(emai) hay que añadir el permiso en el manifest.

    private String obtenerIdTelefono() {

        tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        idTelefono = tMgr.getDeviceId();
        Log.d("PRUEBAS", idTelefono);
        return idTelefono;
    }
}

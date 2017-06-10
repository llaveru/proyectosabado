package com.example.hpasarin.sistemalogin2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hpasarin on 05/06/2017.
 */

public class Login extends Fragment {
    String enteredUsername;
    String enteredPassword;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.layout_fragment_login,container);
        final String serverUrl = "http://www.motosmieres.com/androidlogin/index.php";
        final EditText tvUser = (EditText) vista.findViewById(R.id.tvUser);
        final EditText tvPass = (EditText) vista.findViewById(R.id.tvPass);
        Button botonLogin = (Button) vista.findViewById(R.id.botonLogin);


        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enteredUsername = tvUser.getText().toString();
                enteredPassword = tvPass.getText().toString();

                if(enteredUsername.equals("") || enteredPassword.equals("")){
                    Toast.makeText(getActivity(), "Usuario o contraseña son obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if(enteredUsername.length() <= 1 || enteredPassword.length() <= 1){
                    Toast.makeText(getActivity(), "Username or password debe tener más de un caracter", Toast.LENGTH_LONG).show();
                    return;
                }


                // request authentication with remote server4
                AsyncDataClass asyncRequestObject = new AsyncDataClass(getActivity());
                asyncRequestObject.execute(serverUrl, enteredUsername, enteredPassword);
                Log.d("PRUEBA", "SE INICIA LA TAREA ASINCRONA, AL ACABAR DEBE CAMBIAR EL FRAGMENT");


            }
        });



        return super.onCreateView(inflater, container, savedInstanceState);

    }


}

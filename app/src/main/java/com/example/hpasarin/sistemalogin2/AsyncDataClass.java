package com.example.hpasarin.sistemalogin2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpasarin on 05/06/2017.
 */
//mejor usar nuevas clases tipo HTTPURLCONNECTION.
 class AsyncDataClass extends AsyncTask<String, Void, String> {
    Fragment fragmentCE;
    Activity activityUI;
    String enteredUsername="";
    TextView tvUserdelUI;

    Boolean loginCorrecto =false;

    public AsyncDataClass(Activity activity) {
    this.activityUI=activity; //asi recogemos la actividad que llama a esta clase asíncrona
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("PRUEBA","CORRIENDO tarea asincrona");


        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);

        //se se crea la conexion contra la url obtenida en el parametro0
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httpPost = new HttpPost(params[0]);

        String jsonResult = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", params[1]));
            nameValuePairs.add(new BasicNameValuePair("password", params[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //Se hace que httpClient ejecute la consulta
            HttpResponse response = httpClient.execute(httpPost);
            //e inmediatamente se obtiene el resultado recibido
            jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("PRUEBA","el JSON OBTENIDO EN DOINBACKGROUND: "+jsonResult);
        //se devuelve el resultado recibido por el servidor para seguir procesando el resultado
        //en onPostExecute.

        return jsonResult;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


        //segun lo que hayamos obtenido como respuesta del servidor:

        Log.d("PRUEBAS","SE HA DEVUELTO DEL SERVIDOR: "+result);

        if(result.equals("") || result == null){
            Toast.makeText(activityUI, "La conexión no ha sido correcta", Toast.LENGTH_LONG).show();
            return;
        }
        //del json solo nos interesa si es 1 o 0, por eso lo parseamos
        int jsonResult = returnParsedJsonObject(result);

        //si es 0 , en la activityUi, que es desde donde lanzamos este async, mostramos un Toast
        if(jsonResult == 0){

            Toast.makeText(activityUI, "no validos", Toast.LENGTH_LONG).show();

             tvUserdelUI = (TextView) activityUI.findViewById(R.id.tvUser);
            enteredUsername = (String) tvUserdelUI.getText().toString();

            Log.d("PRUEBA","El usuario/contraseña no son correctos "+enteredUsername);
            //fragmentCE = ConexionCorrecta.newInstance(enteredUsername,"ERROR USUARIO O PASSWORD NO REGISTRADOS");

            //this.activityUI.getFragmentManager().beginTransaction()
              //      .replace(R.id.contenedor,fragmentCE)
                //    .addToBackStack(null)
                  //  .commit();
            return;
        }
        //si es 1 es que se logeo con exíto. Se pasan a ConexionCorrecta (fragmento) unos argumentos para que muestre mensajes
        //de conexión correcta.
        if(jsonResult == 1){
            Toast.makeText(activityUI, "Logeado con exito", Toast.LENGTH_LONG).show();
            tvUserdelUI = (TextView) activityUI.findViewById(R.id.tvUser);
            enteredUsername = tvUserdelUI.getText().toString();
            Log.d("PRUEBA","_______________________________________Se instancia ConexionCorrecta pasando enteredUsername: "+enteredUsername);
            //aqui una actividad podria iniciar otra nueva activity con startactivity y pasarle el intent
            //pero al estar en fragrment parece que no sale:
            Log.d("PRUEBA","sE INStancia el FRAGMENT CONEXIONCORRECTA Con param1 : "+enteredUsername);

            //Se instancia el fragmento ConexionCorrecta, haciendo .newInstance, podemos pasarle argumentos al fragmento.
            fragmentCE = ConexionCorrecta.newInstance(enteredUsername,"Identificado correctamente");


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activityUI.getFragmentManager().beginTransaction()
                    .replace(R.id.contenedorLogin,fragmentCE)
                    .addToBackStack(null)
                    .commit();
        }
    }

    //se procesa el inputStream para convertirlo en un String, se separa esta parte en un metodo para
    //no tenerlotodo junto.

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }



    //devuelve 1 o 0 , segun el json sea success:0 o success:1
    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("PRUEBA","se obtubo en el JSON: "+returnedResult);
        return returnedResult;
    }
}

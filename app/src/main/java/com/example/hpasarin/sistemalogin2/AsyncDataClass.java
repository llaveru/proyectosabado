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

 class AsyncDataClass extends AsyncTask<String, Void, String> {
    Fragment fragmentCE;
    Activity activityUI;
    String enteredUsername="";
    TextView tvUserdelUI;

    public AsyncDataClass(Activity activity) {
    this.activityUI=activity;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("PRUEBA","CORRIENDO tarea asincrona");

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httpPost = new HttpPost(params[0]);

        String jsonResult = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", params[1]));
            nameValuePairs.add(new BasicNameValuePair("password", params[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("PRUEBA","el JSON OBTENIDO EN DOINBACKGROUND: "+jsonResult);
        return jsonResult;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(activityUI, "Server connection failed", Toast.LENGTH_LONG).show();
            return;
        }

        int jsonResult = returnParsedJsonObject(result);
        if(jsonResult == 0){

            Toast.makeText(activityUI, "no validos", Toast.LENGTH_LONG).show();

             tvUserdelUI = (TextView) activityUI.findViewById(R.id.tvUser);
            enteredUsername = (String) tvUserdelUI.getText();

            Log.d("PRUEBA","Se instancia ConexionCorrecta pasando enteredUsername: "+enteredUsername);
            fragmentCE = ConexionCorrecta.newInstance(enteredUsername,"ERROR USUARIO O PASSWORD NO REGISTRADOS");

            this.activityUI.getFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,fragmentCE)
                    .addToBackStack(null)
                    .commit();
            return;
        }
        if(jsonResult == 1){
            //Intent intent = new Intent(activityUI, ConexionExitosa.class);
            //intent.putExtra("USERNAME", enteredUsername);
            //intent.putExtra("MESSAGE", "You have been successfully login");
            tvUserdelUI = (TextView) activityUI.findViewById(R.id.tvUser);
            enteredUsername = tvUserdelUI.getText().toString();
            Log.d("PRUEBA","_______________________________________Se instancia ConexionCorrecta pasando enteredUsername: "+enteredUsername);
            //aqui una actividad podria iniciar otra nueva activity con startactivity y pasarle el intent
            //pero al estar en fragrment parece que no sale:
            Log.d("PRUEBA","sE INStancia el FRAGMENT CONEXIONCORRECTA Con param1 : "+enteredUsername);
            fragmentCE = ConexionCorrecta.newInstance(enteredUsername,"Identificado correctamente");
        Toast.makeText(activityUI,"identificacion correcta",Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activityUI.getFragmentManager().beginTransaction()
                    .replace(R.id.contenedorLogin,fragmentCE)
                    .addToBackStack(null)
                    .commit();
        }
    }
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

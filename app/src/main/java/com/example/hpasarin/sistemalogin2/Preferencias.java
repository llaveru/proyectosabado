package com.example.hpasarin.sistemalogin2;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Preferencias extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_preferencias);
        //no uso la linea de arriba

        //cargo el xml que cree en la carpeta que también cree con nombre xml
        addPreferencesFromResource(R.xml.preferencias);
    }
}

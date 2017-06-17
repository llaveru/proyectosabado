package com.example.hpasarin.sistemalogin2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by seagate on 17/06/2017.
 */

public class ActividadPreferencias extends ActionBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.add(android.R.id.content,new SettingsFragment());
        ft.commit();
    }
}

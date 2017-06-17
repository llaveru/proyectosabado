package com.example.hpasarin.sistemalogin2;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

/**
 * Created by seagate on 17/06/2017.
 */

public class SettingsFragment extends PreferenceFragment {


    public SettingsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}

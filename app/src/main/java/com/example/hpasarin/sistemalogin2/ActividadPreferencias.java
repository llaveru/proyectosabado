package com.example.hpasarin.sistemalogin2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v7.widget.Toolbar;

import android.view.MenuItem;




/**
 * Created by Hector Pasarin seagate on 17/06/2017.
 */

public class ActividadPreferencias extends AppCompatActivity {
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    DrawerLayout drawer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.add(android.R.id.content,new SettingsFragment());
        ft.commit();

      //  addCortinilla();
    }





 /*

    pendiente implementar, da errores

public void addCortinilla()
{
    //hay que poner windowActionBar to false en el theme para usar nuestra toolbar
     toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);




     drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

     toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    assert drawer != null;
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    assert navigationView != null;
    navigationView.setNavigationItemSelectedListener(this);
a
}

*/

/*pendiente hasta que funcione cortina
//se añade funcionalidad al boton de ir hacia atras
  //  @Override
  //  public void onBackPressed() {
    //    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  if (drawer.isDrawerOpen(GravityCompat.START)) {
        //    drawer.closeDrawer(GravityCompat.START);
        //} else {
          //  super.onBackPressed();
       // }
   // }

 */

}

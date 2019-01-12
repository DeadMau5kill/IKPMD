package com.example.mlk.like;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.mikephil.charting.charts.PieChart;

//import com.github.mikephil.charting.charts.PieChart;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SQLiteDatabase mDatabase;
    public static final String database_name = "films_series_data";
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        // Het aantal films tegenover aantal gereviewde series zetten
        String sqlMovie = "SELECT COUNT(*) FROM filmseries WHERE sort ='Movie' ";
        String sqlSerie = "SELECT COUNT(*) FROM filmseries WHERE sort ='Serie' ";

        //Create a cursor that acts as a interface containing the twodimentional Database
        Cursor resultMovie = mDatabase.rawQuery(sqlMovie, null);
        StringBuffer bufferMovie = new StringBuffer();
        while(resultMovie.moveToNext()){
            bufferMovie.append(resultMovie.getString(0));
        }
        Cursor result = mDatabase.rawQuery(sqlSerie, null);
        StringBuffer bufferSerie = new StringBuffer();
        while(result.moveToNext()){
            bufferSerie.append(result.getString(0));
        }

        Integer bufferMovieInt = Integer.valueOf(bufferMovie.toString());
        Integer bufferSerieInt = Integer.valueOf(bufferSerie.toString());
        float[] yData = {bufferMovieInt, bufferSerieInt};
        //PieChart pieChart;
        Log.d(TAG, "onCreate: Created PieChart now");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else if (id == R.id.nav_film) {
            Intent filmsIntent = new Intent(this, FilmActivity.class);
            startActivity(filmsIntent);
        } else if (id == R.id.nav_series) {
            Intent seriesIntent = new Intent(this, SeriesActivity.class);
            startActivity(seriesIntent);
        } else if (id == R.id.nav_stats) {
            Intent statsIntent = new Intent(this, StatsActivity.class);
            startActivity(statsIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

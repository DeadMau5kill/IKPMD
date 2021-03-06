package com.example.mlk.like;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class SeriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SQLiteDatabase mDatabase;
    ArrayList<Series> serieslist = new ArrayList<Series>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(SeriesActivity.this,
                        FormActivity.class);
                startActivity(formIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = openOrCreateDatabase(FormActivity.database_name, MODE_PRIVATE, null);
        listView = (ListView)findViewById(R.id.listViewEmployees);
        loadFilmsFromDB();
    }

    private void loadFilmsFromDB(){
        String sql = "SELECT * FROM filmseries WHERE sort = 'Serie' ";

        //Create a cursor that acts as a interface containing the twodimentional Database
        final Cursor Seriecursor = mDatabase.rawQuery(sql,null);

        if (Seriecursor.moveToFirst()){
            do {
                serieslist.add(new Series(
                        Seriecursor.getInt(0),
                        Seriecursor.getString(1),
                        Seriecursor.getString(2),
                        Seriecursor.getFloat(3),
                        Seriecursor.getString(4),
                        Seriecursor.getString(5),
                        Seriecursor.getFloat(6),
                        Seriecursor.getInt(7)
                ));
            } while (Seriecursor.moveToNext());

            SeriesAdapter adapter1 = new SeriesAdapter(this,
                    R.layout.list_layout_serie, serieslist, mDatabase);
            listView.setAdapter(adapter1);
        }


    };

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
        getMenuInflater().inflate(R.menu.series, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
        } else if (id == R.id.nav_series) {
            Intent seriesIntent = new Intent(this, SeriesActivity.class);
            startActivity(seriesIntent);
        } else if (id == R.id.nav_film) {
            Intent filmsIntent = new Intent(this, FilmActivity.class);
            startActivity(filmsIntent);
        } else if (id == R.id.nav_stats) {
            Intent seriesIntent = new Intent(this, StatsActivity.class);
            startActivity(seriesIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

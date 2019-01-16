package com.example.mlk.like;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SQLiteDatabase mDatabase;
    public static final String database_name = "films_series_data";
    ArrayList<FilmSeries> filmserielist = new ArrayList<FilmSeries>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Opening Database connection and creating a database.
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        listView = (ListView) findViewById(R.id.listview_Main);
        String createQuery = "CREATE TABLE IF NOT EXISTS filmseries (\n" +
                "\tid INTEGER NOT NULL CONSTRAINT filmseries_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "\tname VARCHAR(200),\n" +
                "\treason VARCHAR(500),\n" +
                "\trating FLOAT,\n" +
                "\tsort VARCHAR(15),\n" +
                "\ttime VARCHAR(30),\n" +
                "\tCONSTRAINT checkRating CHECK (rating BETWEEN 1 and 10)\n" +
                "\t);";
        mDatabase.execSQL(createQuery);
        loadFilmsFromDB();
    }


    private void loadFilmsFromDB(){
        String sql = "SELECT * FROM filmseries;";

        //Create a cursor that acts as a interface containing the twodimentional Database
        final Cursor FilmSeriecursor = mDatabase.rawQuery(sql,null);

        if (FilmSeriecursor.moveToFirst()){
            do {
                filmserielist.add(new FilmSeries(
                        FilmSeriecursor.getInt(0),
                        FilmSeriecursor.getString(1),
                        FilmSeriecursor.getString(2),
                        FilmSeriecursor.getFloat(3),
                        FilmSeriecursor.getString(4),
                        FilmSeriecursor.getString(5)
                ));
            } while (FilmSeriecursor.moveToNext());

            filmSerieAdapter adapter = new filmSerieAdapter(this, R.layout.list_layout_filmserie,filmserielist, mDatabase);
            listView.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
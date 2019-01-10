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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FilmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //mDatabase is a SQLiteDatabase
    SQLiteDatabase mDatabase;
    ArrayList<Films> filmslist = new ArrayList<Films>();
    ListView listView;
    //Button btnDelete = (Button) findViewById(R.id.buttonDeleteEmployee);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(FilmActivity.this, FormActivity.class);
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

        //Open the database
        mDatabase = openOrCreateDatabase(FormActivity.database_name, MODE_PRIVATE, null);
        listView = (ListView) findViewById(R.id.listViewEmployees);
        /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FilmActivity.this, filmslist.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });*/


        loadFilmsFromDB();
    }

    private void loadFilmsFromDB(){
        String sql = "SELECT * FROM filmseries WHERE sort ='Movie' ";

        //Create a cursor that acts as a interface containing the twodimentional Database
        final Cursor Filmcursor = mDatabase.rawQuery(sql,null);

        if (Filmcursor.moveToFirst()){
            do {
                filmslist.add(new Films(
                        Filmcursor.getInt(0),
                        Filmcursor.getString(1),
                        Filmcursor.getString(2),
                        Filmcursor.getInt(3),
                        Filmcursor.getString(4),
                        Filmcursor.getString(5)
                ));
            } while (Filmcursor.moveToNext());

            FilmsAdapter adapter = new FilmsAdapter(this, R.layout.list_layout_film,filmslist);
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
        getMenuInflater().inflate(R.menu.film, menu);
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
            Intent seriesIntent = new Intent(this, StatsActivity.class);
            startActivity(seriesIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

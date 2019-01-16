package com.example.mlk.like;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import java.lang.reflect.Array;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

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
        // referencing to the piechart in contents_stats.xml
        PieChartView pieChartView = findViewById(R.id.piechart);
        // creating array to add data to for piechart
        List<SliceValue> pieData = new ArrayList<>();
        //adding data
        pieData.add(new SliceValue(bufferMovieInt, Color.BLUE).setLabel("Movies: " + bufferMovieInt
                .toString()));
        pieData.add(new SliceValue(bufferSerieInt, Color.RED).setLabel("Series: " + bufferSerieInt
                .toString()));
        // Adding data to piechart
        PieChartData pieChartData = new PieChartData(pieData);
        // Creating chart
        pieChartView.setPieChartData(pieChartData.setHasLabels(true));

        //SQL strings klaarmaken
        String sqlAverageScore = "SELECT AVG(rating) FROM filmseries";
        String sqlAverageScoreMovie = "SELECT AVG(rating) FROM filmseries WHERE sort = 'Movie'";
        String sqlAverageScoreSerie = "SELECT AVG(rating) FROM filmseries WHERE sort = 'Serie'";

        Cursor avgScore = mDatabase.rawQuery(sqlAverageScore, null);
        StringBuffer bufferAvgScore = new StringBuffer();
        while(avgScore.moveToNext()){
            bufferAvgScore.append(avgScore.getString(0));
        }

        Cursor avgScoreMovie = mDatabase.rawQuery(sqlAverageScoreMovie, null);
        StringBuffer bufferAvgScoreMovie = new StringBuffer();
        while (avgScoreMovie.moveToNext()){
            bufferAvgScoreMovie.append(avgScoreMovie.getString(0));
        }

        Cursor avgScoreSerie = mDatabase.rawQuery(sqlAverageScoreSerie, null);
        StringBuffer bufferAvgScoreSerie = new StringBuffer();
        while (avgScoreSerie.moveToNext()){
            bufferAvgScoreSerie.append(avgScoreSerie.getString(0));
        }

        String avg = bufferAvgScore.toString();
        String avgMovie = bufferAvgScoreMovie.toString();
        String avgSerie = bufferAvgScoreSerie.toString();

        TextView textAvgScore = (TextView) findViewById(R.id.textAvgScore);
        TextView textAvgScoreMovie = (TextView) findViewById(R.id.textAvgScoreMovie);
        TextView textAvgScoreSerie = (TextView) findViewById(R.id.textAvgScoreSerie);

        textAvgScore.setText("Het gemiddelde score die gegeven is: " + avg);
        textAvgScoreMovie.setText("Het gemiddelde getal die gegeven is voor een film is: "
                + avgMovie);
        textAvgScoreSerie.setText("Het gemiddelde getal die gegeven is voor een film is: "
                + avgSerie);

        //SQL strings maken
        String top5sql = "SELECT * FROM filmseries ORDER BY rating DESC LIMIT 5";
        Cursor top5Score = mDatabase.rawQuery(top5sql, null);
        List<Float> items = new ArrayList<Float>();
        while (top5Score.moveToNext()){
            items.add(top5Score.getFloat(3));
        }
        ColumnChartView colum_chart = (ColumnChartView) findViewById(R.id.columnchart);
        ColumnChartData colum_data;
        int numSubcolums = 1;
        int numColums = items.size();
        List<Column> colums = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColums; i++) {
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(items.get(i), ChartUtils.pickColor()));
            Column colum = new Column(values);
            colum.setHasLabels(true);
            colums.add(colum);
        }
        colum_data = new ColumnChartData(colums);
        Axis axisX = new Axis().setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Films");
        axisY.setName("Rating");
        colum_data.setAxisYLeft(axisY);
        colum_chart.setColumnChartData(colum_data);
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

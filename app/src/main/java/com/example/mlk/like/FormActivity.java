package com.example.mlk.like;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {
    RequestQueue queue;

    //Database name + Object creation
    public static final String database_name = "films_series_data";

    SQLiteDatabase mDatabase;

    EditText textName, textComment, textRating;
    Spinner sort_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_form);

        //Create the database and insert the table calling "createTable()"
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        //Create objects for each palette
        sort_spinner = findViewById(R.id.sort_spinner);
        textName = findViewById(R.id.textName);
        textComment = findViewById(R.id.textComment);
        textRating = findViewById(R.id.textRating);
        findViewById(R.id.buttonSubmit).setOnClickListener(this);
    }

    private void findMovieByTitle(final String title) {
        String url = "https://www.omdbapi.com/?apikey=b053236b&t=" + title.replace(" ", "+");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ImdbMovie movie = gson.fromJson(response, ImdbMovie.class);
                addTuple(movie);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Film niet gevonden. Ingevoerde data wordt gebruikt!", Toast.LENGTH_LONG).show();
                addTuple(null);
            }
        });

        queue.add(request);
    }

    //Add the movie or series to the DB
    private void addTuple(ImdbMovie movie) {
        //Extract the user input
        String name = textName.getText().toString().trim();
        String imdbRating = null;
        String year = null;

        if (movie != null) {
            name = movie.Title;
            imdbRating = movie.imdbRating;
            year = movie.Year;
        }

        String Comment = textComment.getText().toString().trim();
        String rating = textRating.getText().toString().replace(',', '.').trim();
        String sort = sort_spinner.getSelectedItem().toString();
        String time = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(Calendar.getInstance().getTime());


        String regex = "[+-]?([0-9]*[.])?[0-9]+";
        if (!rating.matches(regex)) {
            Toast.makeText(this, "Wrong imput, use numbers",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (rating.isEmpty() || Comment.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(rating) > 10 || Float.parseFloat(rating) < 1) {
            Toast.makeText(this, "Wrong, the number is too low or high", Toast.LENGTH_SHORT).show();
        } else if (rating.matches(regex)) {
            String updateQuery = "INSERT INTO filmseries (name, reason, rating, sort, time, imdb_rating, omdb_year)\n"
                    + "VALUES (?,?,?,?,?,?,?)";
            mDatabase.execSQL(updateQuery, new String[]{name, Comment, rating, sort, time, imdbRating, year});

            //Bring the user to MainActivity when the operation is done
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            // Notify the user that the operation succeeded
            Toast.makeText(this, "Your like has been added!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmit:
                findMovieByTitle(textName.getText().toString().trim());
                break;
        }
    }
}
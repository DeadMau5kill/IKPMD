package com.example.mlk.like;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FormActivity extends AppCompatActivity implements View.OnClickListener{
    String year;
    String imdbRating;
    //Database name + Object creation
    public static final String database_name = "films_series_data";
    SQLiteDatabase mDatabase;
    EditText textName, textComment, textRating;
    Spinner sort_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //Create the database and insert the table calling "createTable()"
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        //Create objects for each palette
        sort_spinner = (Spinner) findViewById(R.id.sort_spinner);
        textName = (EditText) findViewById(R.id.textName);
        textComment = (EditText) findViewById(R.id.textComment);
        textRating = (EditText) findViewById(R.id.textRating);
        findViewById(R.id.buttonSubmit).setOnClickListener(this);

    }

    //Add the movie or series to the DB
    private void addTuple() {
        //Extract the user input
        final String name = textName.getText().toString().trim();
        String Comment = textComment.getText().toString().trim();
        String rating = textRating.getText().toString().replace(',', '.').trim();
        String sort = sort_spinner.getSelectedItem().toString();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance()
                .getTime());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Create URL
                try {
                    URL OMDBEndpoint = new URL("https://www.omdbapi.com/?apikey=b053236b&t=" + name.replace(" ","+"));
                    // Create connection
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) OMDBEndpoint.openConnection();
                    myConnection.setRequestProperty("User-Agent", "Cool school project");
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginObject(); // Start processing the JSON object
                    while (jsonReader.hasNext()) { // Loop through all keys
                        String key = jsonReader.nextName(); // Fetch the next key
                        if (key.equals("Year")) { // Check if desired key
                            // Fetch the value as a String
                            year =jsonReader.nextString();
                            System.out.println("KUT" + year);
                        }
                        else if(key.equals("imdbRating")){
                            imdbRating = jsonReader.nextString();
                            List<String> lijst = getAPI(year, imdbRating);
                            break; // Break out of the loop
                        }
                        else {
                            jsonReader.skipValue(); // Skip values of other keys
                        }
                        
                    }
                    jsonReader.close();
                    myConnection.disconnect();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        });


        String regex = "[+-]?([0-9]*[.])?[0-9]+";
        if(rating.matches(regex) != true){
            Toast.makeText(this, "Wrong imput, use numbers",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(rating.isEmpty() || Comment.isEmpty() || name.isEmpty()){
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(Float.parseFloat(rating) > 10 || Float.parseFloat(rating) < 1){
            Toast.makeText(this, "Wrong, the number is too low or high", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        else if(rating.matches(regex)){
            String updateQuery = "INSERT INTO filmseries (name, reason, rating, sort, time, imdb_rating, omdb_year)\n" +
                    "VALUES (?,?,?,?,?,?,?)";
            System.out.println("KUT1" + year);
            mDatabase.execSQL(updateQuery, new String[]{name, Comment, rating, sort, time, imdbRating, year });
            return;
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public List getAPI(String year, String imdbRating){
        this.year = year;
        this.imdbRating = imdbRating;
        List<String> apiList = new ArrayList();
        apiList.add(year);
        apiList.add(imdbRating);
        return apiList;
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmit:
                addTuple();
                //Notify the user that the operation succeeded
                //Toast.makeText(this, "Je rating is toegevoegd!", Toast.LENGTH_LONG)
                        //.show();

                //Bring the user to MainActivity when the operation is done
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;

            //case R.id.textView
        }
    }
}
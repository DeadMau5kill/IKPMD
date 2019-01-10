package com.example.mlk.like;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {

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
        String name = textName.getText().toString().trim();
        String Comment = textComment.getText().toString().trim();
        String rating = textRating.getText().toString().trim();
        String sort = sort_spinner.getSelectedItem().toString();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        //Catch when the user submits empty values
        if (name.isEmpty()){
            textName.setError("Geef een naam op");
            textName.requestFocus();
            return;
        }
        if (Comment.isEmpty()){
            textComment.setError("Geef commentaar bij de film of serie");
            textComment.requestFocus();
            return;
        }
        if (rating.isEmpty()){
            textRating.setError("Geef een rating van 1 t/m 10");
            textRating.requestFocus();
            return;
        }

        //The query to update the DB table
        String updateQuery = "INSERT INTO filmseries (name, reason, rating, sort, time)\n" +
                "VALUES (?,?,?,?,?)";
        mDatabase.execSQL(updateQuery, new String[]{name, Comment, rating, sort, time});
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmit:
                addTuple();
                //Notify the user that the operation succeeded
                Toast.makeText(this, "Je rating is toegevoegd!", Toast.LENGTH_LONG).show();

                //Bring the user to MainActivity when the operation is done
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;

            //case R.id.textView
        }
    }
}
package com.example.mlk.like;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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
        createTable();

        //Create objects for each palette
        textName = (EditText) findViewById(R.id.textName);
        textComment = (EditText) findViewById(R.id.textComment);
        textRating = (EditText) findViewById(R.id.textRating);
        sort_spinner = (Spinner) findViewById(R.id.sort_spinner);
        findViewById(R.id.buttonSubmit).setOnClickListener(this);
    }

    //A class that creates the table within the DB
    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS filmseries (\n" +
                "\tid INTEGER NOT NULL CONSTRAINT filmseries_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "\tname VARCHAR(200) NOT NULL,\n" +
                "\treason VARCHAR(500) NOT NULL,\n" +
                "\trating INT NOT NULL,\n" +
                "\tCONSTRAINT checkRating CHECK (rating BETWEEN 1 and 10)\n" +
                ");";
        mDatabase.execSQL(query);
    }

    //Add the movie or series to the DB
    private void addTuple() {
        String name = textName.getText().toString().trim();
        String Comment = textComment.getText().toString().trim();
        String rating = textRating.getText().toString().trim();
        String sort = sort_spinner.getSelectedItem().toString();

        //11:56

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmit:
                addTuple();
                break;
        }
    }
}
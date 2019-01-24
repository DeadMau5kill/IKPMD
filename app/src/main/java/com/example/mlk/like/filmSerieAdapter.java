package com.example.mlk.like;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class filmSerieAdapter extends ArrayAdapter<FilmSeries> {
    Context mCtx;
    int layoutRes;
    List<FilmSeries> filmserielist;
    SQLiteDatabase mDatabase;
    public filmSerieAdapter(Context mCtx, int layoutRes, List<FilmSeries> filmserielist, SQLiteDatabase mDatabase){
        super(mCtx, layoutRes, filmserielist);
        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.filmserielist = filmserielist;
        this.mDatabase = mDatabase;
    };

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(layoutRes, null);

        //Get the textviews
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewReason = view.findViewById(R.id.textViewReason);
        TextView textViewRating = view.findViewById(R.id.textRating);
        TextView textViewSort = view.findViewById(R.id.textSort);
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        TextView textViewIMDBrating = view.findViewById(R.id.textViewIMDBrating);
        TextView textViewYear = view.findViewById(R.id.textViewYear);


        final FilmSeries filmserie = filmserielist.get(position);
        textViewName.setText(filmserie.getName());
        textViewReason.setText(filmserie.getReason());
        textViewRating.setText(String.valueOf(filmserie.getRating()));
        textViewSort.setText(filmserie.getSort());
        textViewTime.setText(filmserie.getTime());
        textViewIMDBrating.setText(String.valueOf(filmserie.getImdb_rating()));
        textViewYear.setText(String.valueOf(filmserie.getOmdb_year()));
        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFilmSerie(filmserie);
            }
        });



        return view;
    }

    private void deleteFilmSerie(final FilmSeries filmserie){
        AlertDialog.Builder delDialog = new AlertDialog.Builder(mCtx);
        delDialog.setTitle("Are you sure?");
        delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sqlDel = "DELETE FROM filmseries WHERE id = ?";
                mDatabase.execSQL(sqlDel, new Integer[]{filmserie.getId()});
                reloadEmployeesFromDatabase();
            }
        });
        delDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

            ;

        });
        AlertDialog alertDialog = delDialog.create();
        alertDialog.show();
    };

    private void reloadEmployeesFromDatabase() {
        Cursor FilmSeriecursor = mDatabase.rawQuery("SELECT * FROM filmseries ORDER BY id DESC LIMIT 5" , null);
        if (FilmSeriecursor.moveToFirst()) {
            filmserielist.clear();
            do {
                filmserielist.add(new FilmSeries(
                        FilmSeriecursor.getInt(0),
                        FilmSeriecursor.getString(1),
                        FilmSeriecursor.getString(2),
                        FilmSeriecursor.getInt(3),
                        FilmSeriecursor.getString(4),
                        FilmSeriecursor.getString(5),
                        FilmSeriecursor.getFloat(6),
                        FilmSeriecursor.getInt(7)
                ));
            } while (FilmSeriecursor.moveToNext());
        }
        FilmSeriecursor.close();
        notifyDataSetChanged();
    }

}
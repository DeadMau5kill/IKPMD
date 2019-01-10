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

public class FilmsAdapter extends ArrayAdapter<Films> {
    Context mCtx;
    int layoutRes;
    List<Films> filmslist;
    SQLiteDatabase mDatabase;
    public FilmsAdapter(Context mCtx, int layoutRes, List<Films> filmslist, SQLiteDatabase mDatabase){
            super(mCtx, layoutRes, filmslist);
            this.mCtx = mCtx;
            this.layoutRes = layoutRes;
            this.filmslist = filmslist;
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

        final Films film = filmslist.get(position);
        textViewName.setText(film.getName());
        textViewReason.setText(film.getReason());
        textViewRating.setText(String.valueOf(film.getRating()));
        textViewSort.setText(film.getSort());
        textViewTime.setText(film.getTime());
        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFilm(film);
            }
        });



        return view;
    }

    private void deleteFilm(final Films film){
        AlertDialog.Builder delDialog = new AlertDialog.Builder(mCtx);
        delDialog.setTitle("Are you sure?");
        delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sqlDel = "DELETE FROM filmseries WHERE id = ?";
                mDatabase.execSQL(sqlDel, new Integer[]{film.getId()});
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
        Cursor Filmcursor = mDatabase.rawQuery("SELECT * FROM filmseries WHERE sort ='Movie'" , null);
        if (Filmcursor.moveToFirst()) {
            filmslist.clear();
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
        }
        Filmcursor.close();
        notifyDataSetChanged();
    }

}

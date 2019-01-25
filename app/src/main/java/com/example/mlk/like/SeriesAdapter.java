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

public class SeriesAdapter extends ArrayAdapter<Series> {
    Context mCtx;
    int layoutRes;
    List<Series> serieslist;
    SQLiteDatabase mDatabase;
    public SeriesAdapter(Context mCtx, int layoutRes, List<Series> serieslist, SQLiteDatabase mDatabase){
        super(mCtx, layoutRes, serieslist);
        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.serieslist = serieslist;
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
        TextView textViewIMDBRating = view.findViewById(R.id.textViewIMDBrating);
        TextView textViewYear = view.findViewById(R.id.textViewYear);

        final Series serie = serieslist.get(position);
        textViewName.setText(serie.getName());
        textViewReason.setText(serie.getReason());
        textViewRating.setText(String.valueOf(serie.getRating()));
        textViewSort.setText(serie.getSort());
        textViewTime.setText(serie.getTime());
        textViewIMDBRating.setText(String.valueOf(serie.getImdb_rating()));
        textViewYear.setText(String.valueOf(serie.getOmdb_year()));
        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSerie(serie);
            }
        });
        return view;
    }

    private void deleteSerie(final Series serie){
        AlertDialog.Builder delDialog = new AlertDialog.Builder(mCtx);
        delDialog.setTitle("Are you sure?");
        delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sqlDel = "DELETE FROM filmseries WHERE id = ?";
                mDatabase.execSQL(sqlDel, new Integer[]{serie.getId()});
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
        Cursor Seriecursor = mDatabase.rawQuery("SELECT * FROM filmseries WHERE sort ='Serie'" , null);
        if (Seriecursor.moveToFirst()) {
            serieslist.clear();
            do {
                serieslist.add(new Series(
                        Seriecursor.getInt(0),
                        Seriecursor.getString(1),
                        Seriecursor.getString(2),
                        Seriecursor.getInt(3),
                        Seriecursor.getString(4),
                        Seriecursor.getString(5),
                        Seriecursor.getFloat(6),
                        Seriecursor.getInt(7)
                ));
            } while (Seriecursor.moveToNext());
        }
        Seriecursor.close();
        notifyDataSetChanged();
    }

}

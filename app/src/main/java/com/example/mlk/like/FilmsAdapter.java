package com.example.mlk.like;
import android.content.Context;
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
    public FilmsAdapter(Context mCtx, int layoutRes, List<Films> filmslist){
            super(mCtx, layoutRes, filmslist);
            this.mCtx = mCtx;
            this.layoutRes = layoutRes;
            this.filmslist = filmslist;
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

        Films film = filmslist.get(position);
        textViewName.setText(film.getName());
        textViewReason.setText(film.getReason());
        textViewRating.setText(String.valueOf(film.getRating()));
        textViewSort.setText(film.getSort());
        textViewTime.setText(film.getTime());

        return view;



    }
}

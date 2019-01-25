package com.example.mlk.like;

public class FilmSeries {
    int id, omdb_year;
    float rating, imdb_rating;
    String name, reason, sort, time;

    public FilmSeries(int id, String name, String reason,float rating, String sort, String time, float imdb_rating, int omdb_year) {
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.rating = rating;
        this.sort = sort;
        this.time = time;
        this.imdb_rating = imdb_rating;
        this.omdb_year = omdb_year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public float getRating() {
        return rating;
    }

    public String getSort() {
        return sort;
    }

    public String getTime() {
        return time;
    }

    public float getImdb_rating(){return imdb_rating;}

    public int getOmdb_year(){return omdb_year;}
}
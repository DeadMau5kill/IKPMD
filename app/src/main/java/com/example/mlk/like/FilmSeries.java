package com.example.mlk.like;

public class FilmSeries {
    int id;
    float rating;
    String name, reason, sort, time;

    public FilmSeries(int id, String name, String reason,float rating, String sort, String time) {
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.rating = rating;
        this.sort = sort;
        this.time = time;
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
}

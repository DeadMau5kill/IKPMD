package com.example.mlk.like;
//In this class we will retrieve the films from the DB
public class Series {
    int id, rating;
    String name, reason, sort, time;

    public Series(int id, String name, String reason,int rating, String sort, String time) {
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

    public int getRating() {
        return rating;
    }

    public String getSort() {
        return sort;
    }

    public String getTime() {
        return time;
    }
}

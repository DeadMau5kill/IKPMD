package com.example.mlk.like;
//In this class we will retrieve the films from the DB
public class Films {
    int id, rating;
    String name, reason, sort, time;

    public Films(int id, int rating, String name, String reason, String sort, String time) {
        this.id = id;
        this.rating = rating;
        this.name = name;
        this.reason = reason;
        this.sort = sort;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getSort() {
        return sort;
    }

    public String getTime() {
        return time;
    }
}

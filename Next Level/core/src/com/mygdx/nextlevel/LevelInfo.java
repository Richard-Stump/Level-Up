package com.mygdx.nextlevel;

import java.sql.Date;

public class LevelInfo {
    private final String id; //must be unique, won't ever change
    private String title;
    private String author;
    private float bestTime;
    private float rating;
    private int difficulty;
    private int playCount;
    private Date dateDownloaded;

    /**
     * Constructor
     * @param id a unique level ID
     */
    public LevelInfo(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
    }


    /*
    Getters:
     */
    public float getBestTime() {
        return bestTime;
    }

    public float getRating() {
        return rating;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getPlayCount() {
        return playCount;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateDownloaded() {
        return dateDownloaded;
    }

    public String getId() {
        return id;
    }

    /*
    Setters:
     */

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBestTime(float bestTime) {
        this.bestTime = bestTime;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateDownloaded(Date dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }
}

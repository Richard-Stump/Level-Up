package com.mygdx.nextlevel;

import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LevelInfo {
    private final String id; //must be unique, won't ever change
    private String title;
    private String author;
    private List<Tag> tags;
    private float bestTime;
    private float rating;
    private int difficulty;
    private int playCount;
    private Date dateDownloaded;
    private Date dateCreated;

    /**
     * Constructor
     * @param id a unique level ID
     */
    public LevelInfo(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
        this.tags = new ArrayList<>();
        this.bestTime = 0;
        this.rating = 0;
        this.difficulty = Difficulty.NONE.ordinal();
        this.playCount = 0;
    }

    /**
     * Constructor
     * @param id unique ID
     * @param title title of level
     * @param author author of level
     */
    public LevelInfo(String id, String title, String author) {
        this(id);
        this.title = title;
        this.author = author;
    }

    /**
     * Add a tag to the tag list
     *
     * @param tag tag to insert
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Remove a tag from the list
     *
     * @param tag tag to remove
     * @return true on success, false on failure
     */
    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    /**
     * Compares this level info object to another
     *
     * @param levelInfo LevelInfo to compare to
     * @return true if they are equal, false otherwise
     */
    public boolean equals(LevelInfo levelInfo) {
        if (id.equals(levelInfo.getId()) &&
        author.equals(levelInfo.getAuthor()) &&
        title.equals(levelInfo.getTitle()) &&
        bestTime == levelInfo.getBestTime() &&
        rating == levelInfo.getRating() &&
        playCount == levelInfo.getPlayCount() &&
        difficulty == levelInfo.getDifficulty() &&
        dateCreated.equals(levelInfo.getDateCreated()) &&
        dateDownloaded.equals(levelInfo.getDateDownloaded()) &&
        tags.equals(levelInfo.getTags())) {

            return true;
        } else if (this.id.equals(levelInfo.id)) {
            System.out.println("Uh oh: there are two LevelInfo objects that have the same id, but do not have" +
                    "the same attributes!");
        }
        return false;
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

    public List<Tag> getTags() {
        return tags;
    }

    public Date getDateDownloaded() {
        return dateDownloaded;
    }

    public Date getDateCreated() {
        return dateCreated;
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

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

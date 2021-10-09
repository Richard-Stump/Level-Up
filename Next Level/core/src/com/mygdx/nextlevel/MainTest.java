package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Interpolation;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;

import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args) {
        DownloadedLevelsDB db = new DownloadedLevelsDB();
        if (db.isDBConnected()) {
            System.out.println("db connected");
        } else {
            return;
        }

        //Sort
        System.out.println("Fetching all levels, sorted by title");
        ArrayList<LevelInfo> sortedByTitle = new ArrayList<>(db.sortByTitle());
        for (LevelInfo i: sortedByTitle) {
            System.out.println("\tLevel title: " + i.getTitle());
        }

        System.out.println();
        System.out.println("Sorting all levels by difficulty");
        ArrayList<LevelInfo> sortedByDifficulty = new ArrayList<>(db.sortByDifficulty());
        for (LevelInfo i: sortedByDifficulty) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tDifficulty: " + i.getDifficulty());
        }

        System.out.println();
        System.out.println("Sorting all levels by rating");
        ArrayList<LevelInfo> sortedByRating = new ArrayList<>(db.sortByRating());
        for (LevelInfo i: sortedByRating) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tRating: " + i.getRating());
        }

        System.out.println();
        System.out.println("Sorting all levels by play count");
        ArrayList<LevelInfo> sortedByPlayCount = new ArrayList<>(db.sortByPlayCount());
        for (LevelInfo i: sortedByPlayCount) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tPlay count: " + i.getPlayCount());
        }

        /*
        System.out.println();
        System.out.println("Sorting all levels by date downloaded");
        ArrayList<LevelInfo> sortedByDateDownloaded = new ArrayList<>(db.sortByDateDownloaded());
        for (LevelInfo i: sortedByDateDownloaded) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tDownloaded on: " + i.getDateDownloaded().toString());
        }

        System.out.println();
        System.out.println("Sorting all levels by date created");
        ArrayList<LevelInfo> sortedByDateCreated = new ArrayList<>(db.sortByDateCreated());
        for (LevelInfo i: sortedByDateCreated) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tDate created: " + i.getDateCreated());
        }

         */

        System.out.println();
        System.out.println("Searching all levels with 'standard' and 'bossbattle' tag");
        ArrayList<Tag> searchStandard = new ArrayList<>();
        searchStandard.add(Tag.STANDARD);
        searchStandard.add(Tag.BOSSBATTLE);
        ArrayList<LevelInfo> searchByTag = new ArrayList<>(db.searchByTags(searchStandard));
        for (LevelInfo i: searchByTag) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tTags: " + i.getTags().toString());
        }

        //Search
        System.out.println();
        String toSearchFor = "this";
        System.out.println("Searching for titles with '" + toSearchFor + "' in it");
        ArrayList<LevelInfo> levels = new ArrayList<>(db.searchByTitle(toSearchFor));
        for (LevelInfo i: levels) {
            System.out.println("\tLevel title: " + i.getTitle());
        }

        System.out.println();
        toSearchFor = "Trenton";
        System.out.println("Searching author '" + toSearchFor + "'");
        ArrayList<LevelInfo> authorLevels = new ArrayList<>(db.searchByAuthor(toSearchFor));
        System.out.println(toSearchFor + " made " + authorLevels.size() + " levels:");
        for (LevelInfo i: authorLevels) {
            System.out.println("\tLevel title: " + i.getTitle());
        }

        System.out.println();
        int difficultyToSearch = 6;
        System.out.println("Searching by difficulty " + difficultyToSearch);
        ArrayList<LevelInfo> levelsDif = new ArrayList<>(db.searchByDifficulty(difficultyToSearch));
        for (LevelInfo i: levelsDif) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tDifficulty: " + i.getDifficulty());
        }
    }
}

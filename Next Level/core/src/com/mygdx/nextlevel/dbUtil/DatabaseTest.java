package com.mygdx.nextlevel.dbUtil;

import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.enums.Tag;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {
        DownloadedLevelsDB db = new DownloadedLevelsDB();
        if (db.isDBActive()) {
            System.out.println("db connected");
        } else {
            return;
        }

        //Add 3 levels for testing

        LevelInfo l1 = new LevelInfo("asdf1", "This is test 1", "Trenton");
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(Tag.STANDARD);
        tags1.add(Tag.PUZZLE);
        l1.setTags(tags1);
        l1.setRating((float) 4.5);
        l1.setDifficulty(3);
        l1.setDateDownloaded(new Date(202020));
        l1.setPlayCount(34);
        l1.setDateCreated(new Date(105062));
        l1.setBestTime((float) 164.26);

        LevelInfo l2 = new LevelInfo("qual2", "Quality level this is", "James");
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(Tag.STANDARD);
        tags2.add(Tag.BOSSBATTLE);
        l2.setTags(tags2);
        l2.setRating((float) 5);
        l2.setDifficulty(9);
        l2.setDateDownloaded(new Date(156545));
        l2.setPlayCount(9062);
        l2.setDateCreated(new Date(951320));
        l2.setBestTime((float) 43.75);

        LevelInfo l3 = new LevelInfo("pi3ngi9", "Calc 3 test", "Trenton");
        List<Tag> tags3 = new ArrayList<>();
        tags3.add(Tag.BOSSBATTLE);
        tags3.add(Tag.SPEEDRUN);
        l3.setTags(tags3);
        l3.setRating((float) 0.05);
        l3.setDifficulty(500);
        l3.setDateDownloaded(new Date(222222));
        l3.setPlayCount(4);
        l3.setDateCreated(new Date(666666));
        l3.setBestTime((float) 500.00);

        db.addLevelInfo(l1);
        db.addLevelInfo(l2);
        db.addLevelInfo(l3);

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
        System.out.println("Searching all levels with 'standard' tag");
        ArrayList<Tag> searchStandard = new ArrayList<>();
        searchStandard.add(Tag.STANDARD);
        ArrayList<LevelInfo> searchByTagS = new ArrayList<>(db.searchByTags(searchStandard));
        for (LevelInfo i: searchByTagS) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tTags: " + i.getTags().toString());
        }

        System.out.println();
        System.out.println("Searching all levels with 'bossbattle' tag");
        ArrayList<Tag> searchBoss = new ArrayList<>();
        searchBoss.add(Tag.BOSSBATTLE);
        ArrayList<LevelInfo> searchByTagBB = new ArrayList<>(db.searchByTags(searchBoss));
        for (LevelInfo i: searchByTagBB) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tTags: " + i.getTags().toString());
        }

        System.out.println();
        System.out.println("Combining the two tag lists:");
        List<LevelInfo> combinedList = db.combineLists(searchByTagS, searchByTagBB);
        for (LevelInfo i: combinedList) {
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
        int difficultyToSearch = 3;
        System.out.println("Searching by difficulty " + difficultyToSearch);
        ArrayList<LevelInfo> levelsDif = new ArrayList<>(db.searchByDifficulty(difficultyToSearch));
        for (LevelInfo i: levelsDif) {
            System.out.println("\tLevel title: " + i.getTitle() + "\n\t\tDifficulty: " + i.getDifficulty());
        }

        System.out.println(db.removeLevelInfo(l1));
        System.out.println(db.removeLevelInfo(l2));
        System.out.println(db.removeLevelInfo(l3));
        try {
            db.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}

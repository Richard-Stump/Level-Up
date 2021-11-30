package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.screens.LoginScreen;

import java.util.ArrayList;
import java.util.Random;

public class CreatedLevelsDB extends LevelsDBController {

    /**
     * Constructor
     */
    public CreatedLevelsDB() {
        super("created");
    }

    /*
    Functions specific to the "created" table in the levels.sqlite database:
     */

    /**
     * Checks if an ID is unique or not
     *
     * @param id id to check
     * @return 1 if unique, 0 if not unique, -1 on failure
     */
    public int isUniqueID(String id) {
        if (super.isDBActive()) {
            try {
                if (searchByID(id) == null) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                System.out.println("Exception: There are multiple LevelInfo's with this ID");
                return -1;
            }
        } else {
            System.out.println("Not connected to database");
            return -1;
        }
    }

    /**
     * Generates a new unique ID
     *
     * @return String of unique ID
     */
    public String generateUniqueID(String username) {
        String generatedString;
        try {
            do {
                generatedString = username;
                generatedString += '_';
                int numRandom;

                int lowerA = 97;
                int lowerZ = 122;

                Random random = new Random();
                numRandom = random.nextInt(15);
                if (numRandom < 3) {
                    numRandom += 3;
                }

                for (int i = 0; i < numRandom; i++) {
                    generatedString = generatedString.concat(String.valueOf((char) (random.nextInt(lowerZ - lowerA) + lowerA)));
                }
            } while (isUniqueID(generatedString) != 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return generatedString;
    }

    /**
     * Updates local created database to contain what the server has
     *
     * @return the number of levels added
     */
    public int updateCreatedDatabase() {
        ServerDBHandler dbServer = new ServerDBHandler();
        int oldSize = sortByTitle().size();

        for (LevelInfo levelInfo: sortByTitle()) {
            removeLevelInfo(levelInfo.getId());
        }

        ArrayList<LevelInfo> list = dbServer.getUsersCreatedLevels(LoginScreen.getCurAcc());

        for (LevelInfo levelInfo: list) {
            addLevelInfo(levelInfo);
        }
        return list.size() - oldSize;
    }

}

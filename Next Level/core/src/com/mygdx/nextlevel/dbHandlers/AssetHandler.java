package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.Asset;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class AssetHandler {
    private Connection connection;

    public AssetHandler() {
        connection = PostgreSQLConnect.connect();
        if (connection == null) {
            System.out.println("There was an error with connecting to the server database");
        }
    }

    /**
     * Generates a unique asset id
     *
     * @param asset
     * @return unique asset id
     */
    public String generateAssetID(Asset asset) {
        String generatedString;

        if ((asset.author == null) || (asset.author.equals(""))) {
            return null;
        }

        try {
            do {
                generatedString = asset.author;
                generatedString += '_';
                int numRandom;

                int lowerA = 97;
                int lowerZ = 122;

                Random random = new Random();
                numRandom = random.nextInt(20);
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
     * Checks if an asset id is unique or not
     *
     * @param id id to check for
     * @return 1 if unique, 0 if not unique, and -1 on failure
     */
    public int isUniqueID(String id) {
        if (connection != null) {
            try {
                if (searchByID(id) == null) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                System.out.println("Exception: There are multiple assets with this ID");
                return -1;
            }
        } else {
            System.out.println("Not connected to database");
            return -1;
        }
    }

    /**
     * Search for an asset using its id
     * Does not download the asset
     *
     * @param id id to search for
     * @return asset with the id
     */
    public Asset searchByID(String id) {
        String sqlQuery = "SELECT * FROM api.assets WHERE assetid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            Asset asset = new Asset(name, author);
            asset.setAssetID(id);
            return asset;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Download an asset to the assets folder
     *
     * @param id
     */
    public void downloadAsset(String id) {

    }

    /**
     * Download the image to the assets folder
     *
     * @param id
     */
    public void downloadImage(String id) {

    }


    /**
     * Upload an asset to the server database
     *
     * @param asset
     */
    public void uploadAsset(Asset asset) {
        //verify that there is a unique ID, a name, an author, and that the file is not null
        if (asset == null) {
            return;
        }
        if ((asset.getAssetID() == null) || (asset.getAssetID().equals(""))) {
            return;
        }
        if ((asset.name == null) || (asset.name.equals(""))) {
            return;
        }
        if ((asset.author == null) || (asset.author.equals(""))) {
            return;
        }

        String sqlQuery = "INSERT INTO api.assets (assetid, name, author, file) VALUES (?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, asset.getAssetID());
            statement.setString(2, asset.name);
            statement.setString(3, asset.author);

            File fAsset = new File(asset.getAssetID());
            statement.setBinaryStream(4, new FileInputStream(fAsset), (int)fAsset.length());

            statement.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove an asset from the server database
     *
     * @param id
     */
    public void removeAsset(String id) {

    }
}

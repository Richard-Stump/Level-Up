package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.Asset;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import com.mygdx.nextlevel.enums.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
     * Search for an asset using its name
     * Does not download the asset
     *
     * @param name name to search for
     * @return asset with the name
     */
    public List<Asset> searchByName(String name) {
        String sqlQuery = "SELECT * FROM api.assets WHERE name LIKE ? ORDER BY name ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            List<Asset> list = resultAsList(resultSet);

            return list;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Search for an asset using its author
     * Does not download the asset
     *
     * @param author name to search for
     * @return asset with the author
     */
    public List<Asset> searchByAuthor(String author) {
        String sqlQuery = "SELECT * FROM api.assets WHERE author LIKE ? ORDER BY author ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, "%" + author + "%");
            ResultSet resultSet = statement.executeQuery();

            List<Asset> list = resultAsList(resultSet);

            return list;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Download an asset
     *
     * @param id
     */
    public void downloadAsset(String id) {
        String sqlQuery = "SELECT file FROM api.assets WHERE assetid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            byte[] assetBytes = resultSet.getBytes("file");

            try {
                FileOutputStream fosAsset = new FileOutputStream(new File(id));
                fosAsset.write(assetBytes);
                fosAsset.close();
                System.out.println("Saved asset file from server!");
            } catch (Exception e) {
                System.out.println("Couldn't save the asset from the server");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String sqlQuery2 = "UPDATE api.users SET assetsuploaded = array_append(assetsuploaded, ?) WHERE username=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, asset.getAssetID());
            statement.setString(2, asset.name);
            statement.setString(3, asset.author);

            File fAsset = new File(asset.getAssetID());
            statement.setBinaryStream(4, new FileInputStream(fAsset), (int)fAsset.length());

            statement.executeUpdate();

            //update the users table to correlate who "owns" what assets
            PreparedStatement statement1 = connection.prepareStatement(sqlQuery2);
            statement1.setString(1, asset.getAssetID());
            statement1.setString(2, asset.author);
            statement1.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This function sorts all assets in the server alphabetically
     *
     * @return a list of Asset objects that are sorted alphabetically by title
     */
    public List<Asset> sortAllByTitle() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM api.assets ORDER BY name ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Takes a ResultSet and makes a List of Asset's out of it
     *
     * @param resultSet raw resultset
     * @return a list of Asset objects that match the search
     * @throws SQLException if there's an SQL exception
     */
    public List<Asset> resultAsList(ResultSet resultSet) throws SQLException {
        List<Asset> list = new ArrayList<>();

        //cycle through results and add it to the list
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            Asset asset = new Asset(name, author);
            asset.setAssetID(resultSet.getString("assetid"));
            list.add(asset);
        }
        return list;
    }

    /**
     * Remove an asset from the local computer
     *
     * @param id id of asset to remove
     */
    public void removeAssetLocal(String id) {
        File file = new File(id);
        if (file.exists()) {
            if (file.delete()) {
                System.out.printf("%s deleted successfully\n", id);
            } else {
                System.out.printf("Could not delete %s\n, id");
            }
        } else {
            System.out.printf("%s was not detected as a file\n", id);
        }
    }

    /**
     * Remove an asset from the server
     *
     * @param id id of the asset to remove
     */
    public void removeAssetServer(String id) {
        Asset asset = searchByID(id);

        String sqlQueryUsers = "UPDATE api.users SET assetsuploaded = array_remove(assetsuploaded, ?) WHERE username=?;";
        String sqlQueryAssets = "DELETE FROM api.assets WHERE assetid=?;";

        try (PreparedStatement statementUsers = connection.prepareStatement(sqlQueryUsers)) {
            statementUsers.setString(1, id);
            statementUsers.setString(2, asset.author);
            statementUsers.executeUpdate();

            PreparedStatement statementAssets = connection.prepareStatement(sqlQueryAssets);
            statementAssets.setString(1, id);
            statementAssets.executeUpdate();

            //also remove it locally
            removeAssetLocal(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

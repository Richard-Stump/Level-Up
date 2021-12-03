package com.mygdx.nextlevel;

public class Asset {
    private String assetID;
    public String name;
    public String author;
    public String filename;

    public Asset(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }
}

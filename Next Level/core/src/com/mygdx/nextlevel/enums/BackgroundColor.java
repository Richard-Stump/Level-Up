package com.mygdx.nextlevel.enums;

public enum BackgroundColor {
    BLUE("Blue"),
    BROWN("Brown"),
    GREY("Grey"),
    GREEN("Green");

    private String displayName;
    BackgroundColor(String value) {
        this.displayName = value;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String toString() { return displayName; }
}

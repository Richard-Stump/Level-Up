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

    public static BackgroundColor fromString(String str) {
        if(str.equals("Blue"))
            return BLUE;
        else if (str.equals("Brown"))
            return BROWN;
        else if (str.equals("Grey"))
            return GREY;
        else if (str.equals("Green"))
            return GREEN;

        else return BLUE;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String toString() { return displayName; }
}

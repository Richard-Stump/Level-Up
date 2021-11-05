package com.mygdx.nextlevel.enums;

public enum Difficulty {
    NONE("NONE"),
    VERY_EASY("VERY EASY"),
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    HARD("HARD"),
    VERY_HARD("VERY HARD");

    private String displayName;
    Difficulty(String value) {
        this.displayName = value;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String toString() { return displayName; }
}

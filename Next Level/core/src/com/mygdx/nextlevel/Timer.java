package com.mygdx.nextlevel;

public class Timer {
    static long startTime;
    static long endTime;
    public Timer() {
        startTime = 0;
        endTime = 0;
    }

    public static long getStartTime() {
        startTime = System.nanoTime();
        return startTime;
    }

    public static long getEndTime() {
        endTime = System.nanoTime();
        return endTime;
    }
}

package me.adbi.eftw.business.util;

public final class IDGenerator {
    private static int treeID = 0;
    private static int woodID = 0;
    private static int monkeyID = 0;

    public static int getTreeID() {
        return treeID++;
    }

    public static int getMonkeyID() {
        return monkeyID++;
    }

    public static int getWoodID() {
        return woodID++;
    }
}

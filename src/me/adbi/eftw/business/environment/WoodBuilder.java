package me.adbi.eftw.business.environment;
import me.adbi.eftw.business.entity.Tree;
import me.adbi.eftw.business.util.IDGenerator;
import me.adbi.eftw.dataaccess.DBWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WoodBuilder {
    public static Wood getWood(int size, Map map, String path, DBWriter db) {
        Random r = new Random(100); // Random r = new Random(100);
        List<Tree> trees = new ArrayList<>();
        int n = 0;
        while (n < size) {
            Tree t = new Tree(IDGenerator.getTreeID(), getRandomNumber(map.getMinX(), map.getMaxX()), getRandomNumber(map.getMinY(), map.getMaxY()));
            if (!trees.contains(t)) {
                trees.add(t);
                n++;
            }
        }
        Wood w = new Wood(IDGenerator.getWoodID(), trees, map, path, db);
        return w;
    }

    private static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
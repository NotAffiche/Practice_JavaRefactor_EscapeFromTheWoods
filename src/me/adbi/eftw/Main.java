package me.adbi.eftw;

import me.adbi.eftw.business.entity.Tree;
import me.adbi.eftw.business.enums.DBType;
import me.adbi.eftw.business.environment.*;
import me.adbi.eftw.business.util.IDGenerator;
import me.adbi.eftw.dataaccess.DBWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=====================\n=====================");
        System.out.println("Escape From The Woods");
        System.out.println("=====================\n=====================\n");
        String mySqlConnectionString = "blank";
        String mongoDbConnectionString = "blank";
        DBWriter db = new DBWriter(mongoDbConnectionString, DBType.MONGO);
        String path = "C:\\NET\\monkeys";

        WoodMap m1 = new WoodMap(0, 500, 0, 500);
        Wood w1 = WoodBuilder.getWood(500, m1, path, db);
        w1.placeMonkey("Alice", IDGenerator.getMonkeyID());
        w1.placeMonkey("Janice", IDGenerator.getMonkeyID());
        w1.placeMonkey("Toby", IDGenerator.getMonkeyID());
        w1.placeMonkey("Mindy", IDGenerator.getMonkeyID());
        w1.placeMonkey("Jos", IDGenerator.getMonkeyID());

        WoodMap m2 = new WoodMap(0, 200, 0, 400);
        Wood w2 = WoodBuilder.getWood(2500, m2, path, db);
        w2.placeMonkey("Tom", IDGenerator.getMonkeyID());
        w2.placeMonkey("Jerry", IDGenerator.getMonkeyID());
        w2.placeMonkey("Tiffany", IDGenerator.getMonkeyID());
        w2.placeMonkey("Mozes", IDGenerator.getMonkeyID());
        w2.placeMonkey("Jebus", IDGenerator.getMonkeyID());

        WoodMap m3 = new WoodMap(0, 400, 0, 400);
        Wood w3 = WoodBuilder.getWood(2000, m3, path, db);
        w3.placeMonkey("Kelly", IDGenerator.getMonkeyID());
        w3.placeMonkey("Kenji", IDGenerator.getMonkeyID());
        w3.placeMonkey("Kobe", IDGenerator.getMonkeyID());
        w3.placeMonkey("Kendra", IDGenerator.getMonkeyID());

        List<Wood> woodList = new ArrayList<>() {{add(w1); add(w2); add(w3);}};

        System.out.println("Start");
        //timer
        long startTime = System.nanoTime();

        //multithreading
        List<Thread> threads = new ArrayList<>();
        for (Wood w : woodList) {
            threads.add(new Thread(w, "THREAD-Wood#" + w.getWoodID()));
        }
        for (Thread t : threads) {
            System.out.printf("Starting thread: %s\n", t.getName());
            t.start();
            t.join();
        }

        //time+program end
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        // nano time to milliseconds
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        System.out.println("Elapsed time: " + elapsedTimeMs + " ms");
        System.out.println("End");
    }
}

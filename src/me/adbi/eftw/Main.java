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
    public static void main(String[] args) {
        System.out.println("Escape From The Woods\n");
        String mySqlConnectionString = "def";
        String mongoDbConnectionString = "abc";
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

        System.out.println("Start");
        //timer
        long startTime = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture<List<Tree>> escapeFuture1 = w1.escapeAsync(m1);
        CompletableFuture<List<Tree>> escapeFuture2 = w2.escapeAsync(m2);
        CompletableFuture<List<Tree>> escapeFuture3 = w3.escapeAsync(m3);

        try {
            CompletableFuture<Void> escapeAndBitmapFuture1 = escapeFuture1.thenComposeAsync(routes -> {
                if (routes != null) {
                    CompletableFuture<Void> bitmapFuture = w1.writeEscapeRoutesToBitmapAsync(Collections.singletonList(routes));
                    return CompletableFuture.allOf(escapeFuture1, bitmapFuture);
                } else {
                    return CompletableFuture.completedFuture(null);
                }
            }, executor);

            CompletableFuture<Void> escapeAndBitmapFuture2 = escapeFuture2.thenComposeAsync(routes -> {
                if (routes != null) {
                    CompletableFuture<Void> bitmapFuture = w2.writeEscapeRoutesToBitmapAsync(Collections.singletonList(routes));
                    return CompletableFuture.allOf(escapeFuture2, bitmapFuture);
                } else {
                    return CompletableFuture.completedFuture(null);
                }
            }, executor);

            CompletableFuture<Void> escapeAndBitmapFuture3 = escapeFuture3.thenComposeAsync(routes -> {
                if (routes != null) {
                    CompletableFuture<Void> bitmapFuture = w3.writeEscapeRoutesToBitmapAsync(Collections.singletonList(routes));
                    return CompletableFuture.allOf(escapeFuture3, bitmapFuture);
                } else {
                    return CompletableFuture.completedFuture(null);
                }
            }, executor);

            CompletableFuture<Void> woodDbFuture1 = w1.writeWoodToDBAsync();
            CompletableFuture<Void> woodDbFuture2 = w2.writeWoodToDBAsync();
            CompletableFuture<Void> woodDbFuture3 = w3.writeWoodToDBAsync();

            CompletableFuture.allOf(escapeAndBitmapFuture1, escapeAndBitmapFuture2, escapeAndBitmapFuture3).get(); // Wait for escape and bitmap tasks for each wood
            CompletableFuture.allOf(woodDbFuture1, woodDbFuture2, woodDbFuture3).get(); // Wait for the wood DB writing to complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        executor.shutdown();

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        // Convert elapsed time to milliseconds
        double elapsedTimeMs = elapsedTime / 1_000_000.0;

        System.out.println("Elapsed time: " + elapsedTimeMs + " ms");

        System.out.println("End");
    }
}

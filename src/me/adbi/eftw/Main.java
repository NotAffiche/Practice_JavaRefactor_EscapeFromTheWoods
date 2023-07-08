package me.adbi.eftw;

import me.adbi.eftw.business.enums.DBType;
import me.adbi.eftw.business.environment.*;
import me.adbi.eftw.business.util.IDGenerator;
import me.adbi.eftw.dataaccess.DBWriter;

public class Main {
    public static void main(String[] args) {
        //(Refactoring HoGent async assignment from C# to Java as prep for ?Volvo Internship?)
        System.out.println("Escape From The Woods\n");
        String mySqlConnectionString = "def";
        String mongoDbConnectionString = "abc";
        DBWriter db = new DBWriter(mongoDbConnectionString, DBType.MONGO);
        String path = "C:\\NET\\monkeys";

        Map m1 = new Map(0, 500, 0, 500);
        Wood w1 = WoodBuilder.getWood(500, m1, path,db);
        w1.placeMonkey("Alice", IDGenerator.getMonkeyID());
        w1.placeMonkey("Janice", IDGenerator.getMonkeyID());
        w1.placeMonkey("Toby", IDGenerator.getMonkeyID());
        w1.placeMonkey("Mindy", IDGenerator.getMonkeyID());
        w1.placeMonkey("Jos", IDGenerator.getMonkeyID());

        Map m2 = new Map(0, 200, 0, 400);
        Wood w2 = WoodBuilder.getWood(2500, m2, path,db);
        w2.placeMonkey("Tom", IDGenerator.getMonkeyID());
        w2.placeMonkey("Jerry", IDGenerator.getMonkeyID());
        w2.placeMonkey("Tiffany", IDGenerator.getMonkeyID());
        w2.placeMonkey("Mozes", IDGenerator.getMonkeyID());
        w2.placeMonkey("Jebus", IDGenerator.getMonkeyID());

        Map m3 = new Map(0, 400, 0, 400);
        Wood w3 = WoodBuilder.getWood(2000, m3, path,db);
        w3.placeMonkey("Kelly", IDGenerator.getMonkeyID());
        w3.placeMonkey("Kenji", IDGenerator.getMonkeyID());
        w3.placeMonkey("Kobe", IDGenerator.getMonkeyID());
        w3.placeMonkey("Kendra", IDGenerator.getMonkeyID());

        System.out.println("Start");

        w1.writeWoodToDB();
        w1.escape(m1);

        w2.writeWoodToDB();
        w2.escape(m2);

        w3.writeWoodToDB();
        w3.escape(m3);
    }
}
package me.adbi.eftw.business.environment;

import me.adbi.eftw.business.entity.Monkey;
import me.adbi.eftw.business.entity.Tree;
import me.adbi.eftw.business.grid.TreeGrid;
import me.adbi.eftw.business.grid.XYBoundary;
import me.adbi.eftw.dataaccess.DBWriter;
import me.adbi.eftw.dataaccess.dao.DBMonkeyRecord;
import me.adbi.eftw.dataaccess.dao.DBWoodRecord;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import java.util.*;

public final class Wood implements Runnable {

    //region CTOR
    public Wood(int woodID, List<Tree> trees, WoodMap map, String path, DBWriter db) {
        this.woodID = woodID;
        this.trees = trees;
        this.monkeys = new ArrayList<>();
        this.map = map;
        this.path = path;
        this.db = db;
    }
    //endregion

    //region ATTRIB
    private static final int DRAWING_FACTOR = 16;
    private String path;
    private DBWriter db;
    private Random r = new Random(1);
    private final int woodID;
    private List<Tree> trees;
    private List<Monkey> monkeys;
    private WoodMap map;
    //endregion

    //region PROPS
    public int getWoodID() {
        return woodID;
    }
    //endregion

    //region METHODS
    public void placeMonkey(String monkeyName, int monkeyID) {
        int treeNr;
        do {
            treeNr = r.nextInt(trees.size() - 1);
        } while (trees.get(treeNr).hasMonkey());
        Monkey m = new Monkey(monkeyID, monkeyName, trees.get(treeNr));
        monkeys.add(m);
        trees.get(treeNr).setHasMonkey(true);
        System.out.println(m.getName() + " placed on x: " + trees.get(treeNr).getY() + ", y: " + trees.get(treeNr).getY());
    }
    public void escape(WoodMap map) {
        List<List<Tree>> routes = new ArrayList<>();
        for (Monkey m : monkeys) {
            routes.add(escapeMonkey(m, map));
        }
        writeEscapeRoutesToBitmap(routes);
    }

    private void writeRouteToDB(Monkey monkey, List<Tree> route) {
        System.out.println(String.format("%d:write db routes %d, %s start", woodID, woodID, monkey.getName()));
        List<DBMonkeyRecord> records = new ArrayList<>();
        for (int j = 0; j < route.size(); j++)
        {
            records.add(new DBMonkeyRecord(monkey.getMonkeyId(), monkey.getName(), woodID, j, route.get(j).getTreeId(), route.get(j).getX(), route.get(j).getY()));
        }
        //await db.AsyncWriteMonkeyRecordsMSSQL(records);//old sql serv
        //await db.AsyncWriteMonkeyRecordsMongoDB(records);//new mongodb
        //TODO: Fix db
        System.out.println(String.format("%d:write db routes %d, %s end", woodID, woodID, monkey.getName()));
    }
    public void writeWoodToDB() {
        System.out.printf("%d:write db wood %d start%n", woodID, woodID);
        List<DBWoodRecord> records = new ArrayList<>();
        for (Tree t : trees) {
            records.add(new DBWoodRecord(woodID, t.getTreeId(), t.getX(), t.getY()));
        }
        //await db.AsyncWriteWoodRecordsMSSQL(records);//old sql serv
        //await db.AsyncWriteWoodRecordsMongoDB(records);//new mongodb
        System.out.printf("%d:write db wood %d end%n", woodID, woodID);
    }
    public void writeEscapeRoutesToBitmap(List<List<Tree>> routes) {
        System.out.println(String.format("%d:write bitmap routes %d start", woodID, woodID));
        Color[] cvalues = new Color[] { Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN, Color.MAGENTA };
        BufferedImage bm = new BufferedImage((map.getMaxX() - map.getMinX()) * DRAWING_FACTOR, (map.getMaxY() - map.getMinY()) * DRAWING_FACTOR, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bm.createGraphics();
        int delta = DRAWING_FACTOR / 2;
        Stroke stroke = new BasicStroke(1);
        g.setStroke(stroke);
        for (Tree t : trees) {
            g.setColor(Color.GREEN);
            g.drawOval(t.getX() * DRAWING_FACTOR, t.getY() * DRAWING_FACTOR, DRAWING_FACTOR, DRAWING_FACTOR);
        }
        int colorN = 0;
        for (List<Tree> route : routes) {
            int p1x = route.get(0).getX() * DRAWING_FACTOR + delta;
            int p1y = route.get(0).getY() * DRAWING_FACTOR + delta;
            Color color = cvalues[colorN % cvalues.length];
            g.setColor(color);
            g.drawOval(p1x - delta, p1y - delta, DRAWING_FACTOR, DRAWING_FACTOR);
            g.fillOval(p1x - delta, p1y - delta, DRAWING_FACTOR, DRAWING_FACTOR);
            for (int i = 1; i < route.size(); i++) {
                g.drawLine(p1x, p1y, route.get(i).getX() * DRAWING_FACTOR + delta, route.get(i).getY() * DRAWING_FACTOR + delta);
                p1x = route.get(i).getX() * DRAWING_FACTOR + delta;
                p1y = route.get(i).getY() * DRAWING_FACTOR + delta;
            }
            colorN++;
        }
        g.dispose();
        try {
            File output = new File(path, woodID + "_escapeRoutes.jpg");
            ImageIO.write(bm, "jpg", output);
            System.out.printf("%d:write bitmap routes %d end%n", woodID, woodID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Tree> escapeMonkey(Monkey monkey, WoodMap map) {
        System.out.printf("%d:start %d, %s%n", woodID, woodID, monkey.getName());
        HashSet<Integer> visited = new HashSet<Integer>();
        List<Tree> route = new ArrayList<Tree>();
        route.add(monkey.getTree());
        TreeGrid treeGrid = new TreeGrid(5, new XYBoundary(map.getMinX(), map.getMinY(), map.getMaxX(), map.getMaxY()), trees);
        int n = 25;
        do
        {
            visited.add(monkey.getTree().getTreeId());
            TreeMap<Double, List<Tree>> distanceToMonkey = new TreeMap<>();

            //nearest not visited tree by grid

            int[] arr = findCell(monkey.getTree().getX(), monkey.getTree().getY(), treeGrid);
            int i = arr[0], j = arr[1];
            processCell(distanceToMonkey, treeGrid, i, j, monkey, n, visited);
            int ring = 0;
            while (distanceToMonkey.size() < n)
            {
                ring++;
                processRing(i, j, ring, distanceToMonkey, monkey, n, treeGrid, visited);
            }
            processRing(i, j, ring + 1, distanceToMonkey, monkey, n, treeGrid, visited);

            //distance to border
            //N E S W
            ArrayList<Double> distance = new ArrayList<>();
            distance.add((double)(map.getMaxY() - monkey.getTree().getY()));
            distance.add((double)(map.getMaxX() - monkey.getTree().getX()));
            distance.add((double)(monkey.getTree().getY()-map.getMinY()));
            distance.add((double)(monkey.getTree().getX()-map.getMinX()));

            double distanceToBorder = Collections.min(distance);
            if ((distanceToMonkey.size() == 0) || (distanceToBorder < distanceToMonkey.firstKey()))
            {
                writeRouteToDB(monkey, route);
                System.out.println(String.format("%d:end %d, %s", woodID, woodID, monkey.getName()));
                return route;
            }

            // Add the first tree from the first entry of distanceToMonkey to the route
            Entry<Double, List<Tree>> firstEntry = distanceToMonkey.firstEntry();
            List<Tree> firstEntryValue = firstEntry.getValue();
            route.add(firstEntryValue.get(0));
            monkey.setTree(firstEntryValue.get(0));
        }
        while (true);
    }
    //endregion

    //region METHODS ASYNC
    @Override
    public void run() {
        System.out.println("!! Starting wood async run #ID: " + woodID);
        System.out.println("!! Starting wood db write async run #ID: " + woodID);
        writeWoodToDB();
        System.out.println("!! Ending wood escape async run #ID: " + woodID);
        System.out.println("!! Finished wood escape async run #ID: " + woodID);
        escape(map);
        System.out.println("!! Finished wood escape async run #ID: " + woodID);
        System.out.println("!! Ending wood async run #ID: " + woodID);

    }
    //endregion

    //region METHODS GRID
    private int[] findCell(int x, int y, TreeGrid tg) {
        int[] res = new int[2];
        if (!tg.getXyBoundary().isWithinBounds(x, y)) throw new IllegalArgumentException("Out of bounds");
        int i = (int)((x-tg.getXyBoundary().getMinX()) / tg.getDelta());
        int j = (int)((y-tg.getXyBoundary().getMinY()) / tg.getDelta());
        if (i == tg.getNX()) i--;
        if (j == tg.getNY()) j--;
        res[0] = i;
        res[1] = j;
        return res;
    }
    private void processCell(TreeMap<Double, List<Tree>> dtm, TreeGrid tg, int i, int j, Monkey m, int n, HashSet<Integer> v) {
        for (Tree tree : tg.getTrees()[i][j]) {
            if (!v.contains(tree.getTreeId()) && (!tree.hasMonkey())) {
                double d = Math.sqrt(Math.pow(tree.getX() - m.getTree().getX(), 2) + Math.pow(tree.getY() - m.getTree().getY(), 2));
                if ((dtm.size() < n) || (d < dtm.keySet().size()-1))
                {
                    if (dtm.containsKey(d)) {
                        dtm.get(d).add(tree);
                    }
                    else {
                        List<Tree> arrList = new ArrayList<>();
                        arrList.add(tree);
                        dtm.put(d, arrList);
                    }
                }
            }
        }
    }
    private boolean isValidCell(int i, int j, TreeGrid tg) {
        if ((j < 0) || (j >= tg.getNY())) return false;
        if ((i < 0) || (i >= tg.getNX())) return false;
        return true;
    }
    private void processRing(int i, int j, int ring, TreeMap<Double, List<Tree>> dtm, Monkey m, int n, TreeGrid tg, HashSet<Integer> v) {
        for (int gx=i-ring;gx<=i+ring;gx++)
        {
            int gy = j - ring;
            if (isValidCell(gx, gy, tg)) processCell(dtm, tg, gx, gy, m, n, v);
            gy = j + ring;
            if (isValidCell(gx, gy, tg)) processCell(dtm, tg, gx, gy, m, n, v);
        }
        for (int gy = j - ring; gy <= j + ring - 1; gy++)
        {
            int gx = i - ring;
            if (isValidCell(gx, gy, tg)) processCell(dtm, tg, gx, gy, m, n, v);
            gx = i + ring;
            if (isValidCell(gx, gy, tg)) processCell(dtm, tg, gx, gy, m, n, v);
        }
    }
    //endregion
}
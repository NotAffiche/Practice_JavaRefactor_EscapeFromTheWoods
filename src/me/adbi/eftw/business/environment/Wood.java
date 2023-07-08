package me.adbi.eftw.business.environment;

import me.adbi.eftw.business.entity.Monkey;
import me.adbi.eftw.business.entity.Tree;
import me.adbi.eftw.business.grid.TreeGrid;
import me.adbi.eftw.business.grid.XYBoundary;
import me.adbi.eftw.dataaccess.dao.DBWoodRecord;

import java.util.Map.Entry;

import java.util.*;

final class Wood {

    //region CTOR
    public Wood(int woodID, List<Tree> trees, Map map, String path /*, DBwriter db*/) {
        this.woodID = woodID;
        this.trees = trees;
        this.monkeys = new ArrayList<Monkey>();
        this.map = map;
        this.path = path;
        //this.db = db;
    }
    //endregion

    //region ATTRIB
    private static final int DRAWING_FACTOR = 8;
    private String path;
    //TODO: fix dbwriter
    //private DBwriter db;
    private Random r = new Random(1);
    private final int woodID;
    private List<Tree> trees;
    private List<Monkey> monkeys;
    private Map map;
    //endregion

    //region METHODS
    public void PlaceMonkey(String monkeyName, int monkeyID) {
        int treeNr;
        do {
            treeNr = r.nextInt(trees.size() - 1);
        } while (trees.get(treeNr).hasMonkey());
        Monkey m = new Monkey(monkeyID, monkeyName, trees.get(treeNr));
        monkeys.add(m);
        trees.get(treeNr).setHasMonkey(true);
        System.out.println(m.getName() + " placed on x: " + trees.get(treeNr).getY() + ", y: " + trees.get(treeNr).getY());
    }
    public void Escape(Map map) {
        List<List<Tree>> routes = new ArrayList<>();
        for (Monkey m : monkeys) {
            routes.add(EscapeMonkey(m, map));
        }
        WriteEscaperoutesToBitmap(routes);
    }

    public void WriteWoodToDB() {
        System.out.println(String.format("%d:write db wood %d start", woodID, woodID));
        List<DBWoodRecord> records = new ArrayList<>();
        for (Tree t : trees) {
            records.add(new DBWoodRecord(woodID, t.getTreeId(), t.getX(), t.getY()));
        }
        //await db.AsyncWriteWoodRecordsMSSQL(records);//old sql serv
        //await db.AsyncWriteWoodRecordsMongoDB(records);//new mongodb
        System.out.println(String.format("%d:write db wood %d end", woodID, woodID));
    }
    public List<Tree> EscapeMonkey(Monkey monkey, Map map) {
        System.out.println(String.format("%d:start %d, %s", woodID, woodID, monkey.getName()));
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
                WriteRouteToDB(monkey, route);
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
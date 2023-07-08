package me.adbi.eftw.dataaccess.dao;

public class DBMonkeyRecord {

    //region CTOR
    public DBMonkeyRecord(int monkeyId, String monkeyName, int woodId, int seqNr, int treeId, int x, int y) {
        setMonkeyID(monkeyId);
        setMonkeyName(monkeyName);
        setWoodID(woodId);
        setSeqNr(seqNr);
        setTreeID(treeId);
        setX(x);
        setY(y);
    }
    //endregion

    //region ATTRIB
    private int recordId, monkeyId, woodId, seqNr, treeId, x, y;
    private String monkeyName;
    //endregion

    //region PROPERTIES
    public int getRecordID() { return recordId; }
    private void setRecordID(int id) { recordId = id; }
    public int getWoodID() { return woodId; }
    private void setWoodID(int id) { woodId = id; }
    public int getTreeID() { return treeId; }
    private void setTreeID(int id) { treeId = id;}
    public int getX() { return x; }
    private void setX(int x) { this.x = x; }
    public int getY() { return y; }
    private void setY(int y) { this.y = y; }

    public int getMonkeyID() { return monkeyId; }
    public void setMonkeyID(int id) { monkeyId = id; }
    public String getMonkeyName() { return monkeyName; }
    public void setMonkeyName(String name) { monkeyName = name; }
    public int getSeqNr() { return seqNr; }
    public void setSeqNr(int nr) { seqNr = nr; }
    //endregion
}

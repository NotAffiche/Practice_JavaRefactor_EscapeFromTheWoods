package me.adbi.eftw.dataaccess.dao;

public final class DBWoodRecord {

    //region CTOR
    public DBWoodRecord(int woodId, int treeId, int x, int y) {
        setWoodID(woodId);
        setTreeID(treeId);
        setX(x);
        setY(y);
    }
    //endregion

    //region ATTRIB
    private int recordId, woodId, treeId, x, y;
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
    //endregion
}

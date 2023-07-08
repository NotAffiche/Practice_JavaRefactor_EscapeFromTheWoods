package me.adbi.eftw.business.entity;

public final class Monkey {

    //region CTOR
    public Monkey(int monkeyId, String name, Tree tree) {
        setMonkeyId(monkeyId);
        setName(name);
        setTree(tree);
    }
    //endregion

    //region ATTRIB
    private int _monkeyId;
    private String _name;
    private Tree _tree;
    //endregion

    //region GET&SET
    public int getMonkeyId() { return _monkeyId; }
    private void setMonkeyId(int id) { _monkeyId = id; }
    public String getName() { return _name; }
    private void setName(String name) { _name = name; }
    public Tree getTree() { return _tree; }
    public void setTree(Tree tree) { _tree = tree; }
    //endregion
}

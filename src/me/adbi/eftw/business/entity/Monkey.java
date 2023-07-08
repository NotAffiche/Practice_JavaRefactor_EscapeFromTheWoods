package me.adbi.eftw.business.entity;

final class Monkey {

    //region CTOR
    private Monkey(int monkeyId, String name, Tree tree) {
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
    private int getMonkeyId() { return _monkeyId; }
    private void setMonkeyId(int id) { _monkeyId = id; }
    private String getName() { return _name; }
    private void setName(String name) { _name = name; }
    private Tree getTree() { return _tree; }
    private void setTree(Tree tree) { _tree = tree; }
    //endregion
}

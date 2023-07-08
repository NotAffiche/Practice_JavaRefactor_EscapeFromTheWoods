package me.adbi.eftw.business.entity;

import java.util.Objects;

public final class Tree {

    //region CTOR
    public Tree(int treeId, int x, int y) {
        setTreeId(treeId);
        setX(x);
        setY(y);
        setHasMonkey(false);
    }
    //endregion

    //region ATTRIB
    private int _treeId, _x, _y;
    private boolean _hasMonkey;
    //endregion

    //region GET&SET
    private int getTreeId() { return _treeId; }
    private void setTreeId(int treeId) { _treeId = treeId; }
    public int getX() { return _x; }
    private void setX(int x) { _x = x; }
    public int getY() { return _y; }
    private void setY(int y) { _y = y; }
    private boolean hasMonkey() { return _hasMonkey; }
    private void setHasMonkey(boolean has) { _hasMonkey = has; }
    //endregion

    //region OVERRIDES
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tree tree = (Tree) obj;
        return getX() == tree.getX() && getY() == tree.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
    @Override
    public String toString()
    {
        return String.format("id: %d, x: %d, y: %d", getTreeId(), getX(), getY());
    }
    //endregion
}

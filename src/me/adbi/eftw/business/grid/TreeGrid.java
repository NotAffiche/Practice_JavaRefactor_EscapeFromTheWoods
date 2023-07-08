package me.adbi.eftw.business.grid;

import me.adbi.eftw.business.entity.Tree;

import java.util.List;

final class TreeGrid {

    //region CTOR

    //endregion

    //region ATTRIB
    private int _delta, _nx, _ny;
    private XYBoundary _xyBoundary;
    private List<Tree>[][] _trees;
    //endregion

    //region GET&SET
    private int getDelta() { return _delta; }
    private void setDelta(int delta) {
        this._delta = delta;
    }

    private int getNX() { return _nx; }
    private void setNX(int nx) {
        this._nx = nx;
    }

    private int getNY() { return _ny; }
    private void setNY(int ny) {
        this._ny = ny;
    }

    private XYBoundary getXyBoundary() { return _xyBoundary; }
    private void setXyBoundary(XYBoundary xyBoundary) {
        this._xyBoundary = xyBoundary;
    }

    private List<Tree>[][] getTrees() { return _trees; }
    private void setTrees(List<Tree>[][] trees) {
        this._trees = trees;
    }
    //endregion

    //region METHODS
    private void AddTree(Tree tree) {
        if ((tree.getX() < getXyBoundary().getMinX()) || (tree.getY() < getXyBoundary().getMinY()) ||
                (tree.getX() > getXyBoundary().getMaxX()) || (tree.getY() > getXyBoundary().getMaxY())) throw new IllegalArgumentException("Out of bounds");
        int i = (int)((tree.getX() - getXyBoundary().getMinX()) / getDelta());
        int j = (int)((tree.getY() - getXyBoundary().getMinY()) / getDelta());
        if (i == getNX()) i--;
        if (j == getNY()) j--;
        getTrees()[i][j].add(tree);
    }
    //endregion
}

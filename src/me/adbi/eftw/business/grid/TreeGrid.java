package me.adbi.eftw.business.grid;

import me.adbi.eftw.business.entity.Tree;

import java.util.ArrayList;
import java.util.List;

public final class TreeGrid {

    //region CTOR
    private TreeGrid(int delta, XYBoundary xyBoundary) {
        setDelta(delta);
        setXyBoundary(xyBoundary);
        setNX((int)(getXyBoundary().getDX() / delta) + 1);
        setNY((int)(getXyBoundary().getDY() / delta) + 1);

        setTrees(new ArrayList[getNX()][getNY()]);
        for (int i = 0; i < getNX(); i++) {
            getTrees()[i] = new ArrayList[getNY()];
            for (int j = 0; j < getNY(); j++) {
                getTrees()[i][j] = new ArrayList<>();
            }
        }
    }

    public TreeGrid(int delta, XYBoundary xyb, List<Tree> data) {
        this(delta, xyb);
        for (Tree t : data) {
            AddTree(t);
        }
    }
    //endregion

    //region ATTRIB
    private int _delta, _nx, _ny;
    private XYBoundary _xyBoundary;
    private List<Tree>[][] _trees;
    //endregion

    //region GET&SET
    public int getDelta() { return _delta; }
    private void setDelta(int delta) {
        this._delta = delta;
    }

    public int getNX() { return _nx; }
    private void setNX(int nx) {
        this._nx = nx;
    }

    public int getNY() { return _ny; }
    private void setNY(int ny) {
        this._ny = ny;
    }

    public XYBoundary getXyBoundary() { return _xyBoundary; }
    private void setXyBoundary(XYBoundary xyBoundary) {
        this._xyBoundary = xyBoundary;
    }

    public List<Tree>[][] getTrees() { return _trees; }
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

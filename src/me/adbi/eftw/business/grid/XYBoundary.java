package me.adbi.eftw.business.grid;

final class XYBoundary {

    //region CTOR
    private XYBoundary(int minX, int minY, int maxX, int maxY) {
        setMinX(minX);
        setMinY(minY);
        setMaxX(maxX);
        setMaxY(maxY);
    }
    //endregion

    //region ATTRIB
    private int _minX, _minY, _maxX, _maxY;
    //endregion

    //region GET&SET
    int getMinX() { return _minX; }
    private void setMinX(int x) { _minX = x; }
    int getMinY() { return _minY; }
    private void setMinY(int y) { _minY = y; }
    int getMaxX() { return _maxX; }
    private void setMaxX(int x) { _maxX = x; }
    int getMaxY() { return _maxY; }
    private  void setMaxY(int y) { _maxY = y; }
    //endregion

    //region METHODS
    private int getDX() {
        return getMaxX() - getMinX();
    }
    private int getDY() {
        return getMaxY() - getMinY();
    }

    private boolean isWithinBounds(int x, int y){
        if ((x<getMinX()) || (x>getMaxX()) || (y<getMinY()) || (y>getMaxY())) return false;
        return true;
    }
    //endregion
}

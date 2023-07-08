package me.adbi.eftw.business.environment;

final class Map {

    //region CTOR
    private Map(int minX, int maxX, int minY, int maxY) {
        setMinX(minX);
        setMaxX(maxX);
        setMinY(minY);
        setMaxY(maxY);
    }
    //endregion

    //region ATTRIB
    private int _minX, _minY, _maxX, _maxY;
    //endregion

    //region GET&SET
    int getMinX() { return _minX; }
    private void setMinX(int _xmin) {
        this._minX = _xmin;
    }

    int getMinY() { return _minY; }
    private void setMinY(int _ymin) {
        this._minY = _ymin;
    }

    int getMaxX() { return _maxX; }
    private void setMaxX(int _xmax) {
        this._maxX = _xmax;
    }

    int getMaxY() { return _maxY; }
    private void setMaxY(int _ymax) {
        this._maxY = _ymax;
    }
    //endregion
}

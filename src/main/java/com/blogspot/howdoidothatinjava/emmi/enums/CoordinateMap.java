package com.blogspot.howdoidothatinjava.emmi.enums;

public enum CoordinateMap {
    ONE(1, 0, 0), TWO(2, 0, 1), THREE(3, 0, 2), FOUR(4, 1, 0), FIVE(5, 1, 1), SIX(6, 1, 2), SEVEN(7, 2, 0), EIGHT(8, 2,
            1), NINE(9, 2, 2);

    private Integer xCoord;
    private Integer yCoord;
    private Integer id;

    private CoordinateMap(int id, int xCoord, int yCoord) {
        this.id = Integer.valueOf(id);
        this.xCoord = Integer.valueOf(xCoord);
        this.yCoord = Integer.valueOf(yCoord);
    }

    public int getX() {
        return xCoord;
    }

    public int getY() {
        return yCoord;
    }

    public int getId() {
        return id;
    }

    public static CoordinateMap getMapForId(int id) {
        return CoordinateMap.values()[id - 1];
    }

    public String getPrettyCoordinates() {
        String coords = "(x,y)";
        coords = coords.replace("x", Integer.valueOf(xCoord + 1).toString());
        coords = coords.replace("y", Integer.valueOf(yCoord + 1).toString());
        return coords;
    }

    public static CoordinateMap getCoordinateMapForCoords(String coords) {
        String xVal = coords.substring(1, 2);
        String yVal = coords.substring(3, 4);
        return getCoordinateMapForCoords(Integer.valueOf(xVal) - 1, Integer.valueOf(yVal) - 1);
    }

    public static CoordinateMap getCoordinateMapForCoords(Integer x, Integer y) {
        CoordinateMap result = null;
        for (CoordinateMap m : CoordinateMap.values()) {
            if (m.getX() != x) {
                continue;
            }
            if (m.getY() != y) {
                continue;
            }
            result = m;
            break;
        }
        return result;
    }

}

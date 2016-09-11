package com.blogspot.howdoidothatinjava.emmi.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public enum WinCombination {
    // @formatter:off
    ROW1(WinType.ROW, 0, new Integer[] { 0, 1, 2 }),
    ROW2(WinType.ROW, 1, new Integer[] { 3, 4, 5 }),
    ROW3(WinType.ROW, 2, new Integer[] { 6, 7, 8 }),
    COL1(WinType.COLUMN, 0, new Integer[] { 0, 3, 6 }),
    COL2(WinType.COLUMN, 1, new Integer[] { 1, 4, 7 }),
    COL3(WinType.COLUMN, 2, new Integer[] { 2, 5, 8 }),
    DIAG1(WinType.DIAGONAL_LR, 0, new Integer[] { 0, 4, 8 }),
    DIAG2(WinType.DIAGONAL_RL, 1, new Integer[] { 2, 4, 6 });
    // @formatter:on

    private WinType type;
    private Integer id;
    private Map<Integer, WinCombination> winCombinationMap = new LinkedHashMap<>();
    private Integer[] winIndexArray;

    private WinCombination(WinType type, Integer id, Integer[] winIndexArray) {
        this.type = type;
        this.id = id;
        this.winIndexArray = winIndexArray;
        winCombinationMap.put(Integer.valueOf(this.ordinal()), this);
    }

    private WinCombination getWinCombinationForId(Integer id) {
        return winCombinationMap.get(id);
    }

    public static WinCombination getWinCombinationById(Integer id) {
        // allow the static call to get the WinCombination using any
        // WinCombination value
        return WinCombination.ROW1.getWinCombinationForId(id);
    }

    public String getKeyName() {
        return StringUtils.capitalize(this.toString().toLowerCase());
    }

    public Integer getId() {
        return this.id;
    }

    public WinType getWinType() {
        return this.type;
    }

    public Integer[] getWinIndexArray() {
        return this.winIndexArray;
    }
}

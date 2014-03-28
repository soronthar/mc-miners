package com.soronthar.mc.core;

public enum HarvestLevel {
    WOOD(0),
    STONE(1),
    IRON(2),
    DIAMOND(3),
    GOLD(0);
    private final int level;

    HarvestLevel(int level) {
        this.level=level;
    }

    public int getLevel() {
        return level;
    }
}

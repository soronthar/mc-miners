package com.soronthar.mc.core;

public enum HarvestTool {
    AXE("axe"), SHOVEL("shovel"),PICKAXE("pickaxe");
    private final String tool;

    HarvestTool(String tool) {
        this.tool = tool;
    }

    public String getTool() {
        return tool;
    }
}

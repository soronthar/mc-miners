package com.soronthar.mc.core;

import net.minecraft.inventory.IInventory;

public interface InventoryEntity extends IInventory {
    void dropContent();
}

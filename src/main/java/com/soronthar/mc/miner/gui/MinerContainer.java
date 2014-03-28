package com.soronthar.mc.miner.gui;

import com.soronthar.mc.miner.MinerEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MinerContainer extends Container {
    private MinerEntity miner;

    public MinerContainer(InventoryPlayer invPlayer, MinerEntity entity) {
         this.miner=entity;

        for(int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(invPlayer, x, 8 + x * 18, 142));
        }

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                addSlotToContainer(new Slot(entity, x + y * 3, 62 + x * 18, 17 + y * 18));
            }
        }

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = getSlot(i);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            ItemStack result = itemstack.copy();

            if(i >= 36) {
                if(!mergeItemStack(itemstack, 0, 36, false)) {
                    return null;
                }
            } else if(!mergeItemStack(itemstack, 36, 36 + miner.getSizeInventory(), false)) {
                return null;
            }

            if(itemstack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, itemstack);
            return result;
        }
        return null;
    }



    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return miner.isUseableByPlayer(player);
    }

}

package com.soronthar.mc.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class InventoryManager implements IInventory {
    private ItemStack[] inventory;
    private TileEntity entity;
    private final String name;


    public InventoryManager(String name,TileEntity entity, int capacity) {
        this.entity=entity;
        this.name=name;
        this.inventory=new ItemStack[capacity];
    }


    public void writeToNBT(NBTTagCompound par1, String suffix) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemstack = getStackInSlot(i);

            if (itemstack != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot"+suffix, (byte) i);
                itemstack.writeToNBT(item);
                list.appendTag(item);
            }
        }

        par1.setTag("Items"+suffix, list);
    }

    public void readFromNBT(NBTTagCompound par1, String suffix) {
        NBTTagList list = par1.getTagList("Items"+suffix, 10);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound item = list.getCompoundTagAt(i);
            int slot = item.getByte("Slot"+suffix);
            if (slot >= 0 && slot < getSizeInventory()) {
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
            }
        }
    }

    public boolean mergeStack(ItemStack drop) {
        boolean placed=false;
        for (int i = 0; i < inventory.length &&!placed; i++) {
            ItemStack itemStack = inventory[i];
            if (itemStack == null) {
                inventory[i] = drop;
                placed = true;
            } else if (itemStack.getItem().equals(drop.getItem())
                    && itemStack.isStackable()
                    && itemStack.stackSize + drop.stackSize <= itemStack.getMaxStackSize()) {

                itemStack.stackSize += drop.stackSize;
                placed = true;
            }
        }
        return placed;
    }


    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
                setInventorySlotContents(slot, null);
            } else {
                itemstack = itemstack.splitStack(count);
                this.markDirty();
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemstack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inventory[slot] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }


    @Override
    public String getInventoryName() {
        return this.name;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {

        double xCoord=this.entity.xCoord;
        double yCoord=this.entity.yCoord;
        double zCoord=this.entity.zCoord;
        return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public void openInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void markDirty() {
        this.entity.markDirty();
    }
}

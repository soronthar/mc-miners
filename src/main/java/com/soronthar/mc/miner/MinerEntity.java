package com.soronthar.mc.miner;

import com.soronthar.mc.core.InventoryEntity;
import com.soronthar.mc.core.InventoryManager;
import com.soronthar.mc.core.Util;
import com.soronthar.mc.miner.mining.MiningStrategy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public abstract class MinerEntity extends TileEntity implements InventoryEntity {
    long ticks = 0;
    int facing;
    InventoryManager inventory;
    MiningStrategy strategy;
    protected int ticksPerStep;

    public MinerEntity() {
        super();
        init();
    }

    public MinerEntity(int facing) {
        this.facing = facing;
        init();
    }

    protected void init() {
        this.inventory = new InventoryManager(getClass().getSimpleName(), this, 9);
        this.strategy = createStrategy();
        this.ticksPerStep =20;
    }

    protected abstract MiningStrategy createStrategy();

    @Override
    public void writeToNBT(NBTTagCompound par1) {
        super.writeToNBT(par1);
        par1.setLong("ticks", ticks);
        par1.setInteger("speed", ticksPerStep);
        par1.setInteger("facing", facing);


        inventory.writeToNBT(par1, getClass().getSimpleName());
        strategy.writeToNBT(par1, getClass().getSimpleName());
    }

    @Override
    public void readFromNBT(NBTTagCompound par1) {
        super.readFromNBT(par1);
        ticks = par1.getLong("ticks");
        ticksPerStep = par1.getInteger("speed");
        facing = par1.getInteger("facing");

        inventory.readFromNBT(par1, getClass().getSimpleName());
        strategy.readFromNBT(par1, getClass().getSimpleName());
    }

    @Override
    public void updateEntity() {

        if (ticks == 0) {
            this.facing=getWorldObj().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            strategy.init(xCoord, yCoord, zCoord, facing);
        }
        ticks++;

        if (!worldObj.isRemote && ticks % this.ticksPerStep == 0) {
            World worldObj = getWorldObj();
            List<ItemStack> drops = strategy.step(worldObj);
            placeDropsInInventory(worldObj, drops);
        }

    }

    private void placeDropsInInventory(World worldObj, List<ItemStack> drops) {
        for (ItemStack drop : drops) {
            boolean placed = inventory.mergeStack(drop);
            if (!placed) {
                Util.eject(drop, worldObj, xCoord, yCoord, zCoord);
            } else {
                this.markDirty();
            }
        }
    }

    public void dropContent() {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (stackInSlot != null) {
                Util.eject(stackInSlot, getWorldObj(), xCoord, yCoord, zCoord);
                inventory.decrStackSize(i, stackInSlot.stackSize);
            }
        }
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        return inventory.decrStackSize(slot, count);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return inventory.isItemValidForSlot(i, itemstack);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return inventory.isUseableByPlayer(entityplayer);
    }

    @Override
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inventory.setInventorySlotContents(slot, itemstack);
    }

    @Override
    public void invalidate() {
        this.strategy.stop(getWorldObj());
        super.invalidate();
    }
}

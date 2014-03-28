package com.soronthar.mc.miner.mining;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public interface MiningStrategy {
    void writeToNBT(NBTTagCompound tagCompound, String suffix);

    void readFromNBT(NBTTagCompound tagCompound, String suffix);

    List<ItemStack> step(World worldObj);

    void init(int xCoord, int yCoord, int zCoord, int facing);

    void stop(World worldObj);
}

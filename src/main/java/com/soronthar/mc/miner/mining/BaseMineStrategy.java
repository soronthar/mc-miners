package com.soronthar.mc.miner.mining;

import com.soronthar.mc.core.Vect3i;
import com.soronthar.mc.miner.drill.Drill;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class BaseMineStrategy implements MiningStrategy {
    protected Drill drill=new Drill();
    protected int step;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound, String suffix) {
        this.step = tagCompound.getInteger("step");

        this.drill.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound, String suffix) {

        tagCompound.setInteger("step", this.step);
        this.drill.writeToNBT(tagCompound);

    }

    @Override
    public void init(int xCoord, int yCoord, int zCoord, int facing) {
        drill.facing = facing;
        drill.setDrillPos(new Vect3i(xCoord, yCoord, zCoord));
    }

    @Override
    public void stop(World world) {
        drill.removeDrillBlock(world);
    }

}

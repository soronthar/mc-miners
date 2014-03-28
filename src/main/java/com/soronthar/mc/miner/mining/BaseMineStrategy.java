package com.soronthar.mc.miner.mining;

import com.soronthar.mc.core.Vect3i;
import com.soronthar.mc.miner.drill.Drill;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class BaseMineStrategy implements MiningStrategy {
    protected OreDetector oreDetector = new OreDetector();
    protected Drill drill=new Drill();
    protected int step;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound, String suffix) {
        this.step = tagCompound.getInteger("step");

        this.oreDetector.readFromNBT(tagCompound, "oreDetector");
        this.drill.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound, String suffix) {

        tagCompound.setInteger("step", this.step);
        this.drill.writeToNBT(tagCompound);
        this.oreDetector.writeToNBT(tagCompound, "oreDetector");

    }

    @Override
    public void init(int xCoord, int yCoord, int zCoord, int facing) {
        drill.facing = facing;
        drill.drillPos = new Vect3i(xCoord, yCoord, zCoord);
        oreDetector.moveTo(drill.drillPos,drill.facing);
    }

    @Override
    public void stop(World world) {
        drill.removeDrillBlock(world);
    }

}

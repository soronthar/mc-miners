package com.soronthar.mc.miner.xmine;

import com.soronthar.mc.core.Vect3i;
import com.soronthar.mc.miner.mining.BaseMineStrategy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

//TODO: manage States: (optimization) Drilling, Exploring Ore, dealing with sand
public class XMineLayout extends BaseMineStrategy {
    private static final int STEP = 6;


    private int row;
    private int nextStop;


    @Override
    public void readFromNBT(NBTTagCompound tagCompound, String suffix) {
       super.readFromNBT(tagCompound, suffix);
        this.row = tagCompound.getInteger("row");
        this.nextStop = tagCompound.getInteger("nextStop");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound, String suffix) {
        super.writeToNBT(tagCompound, suffix);
        tagCompound.setInteger("row", this.row);
        tagCompound.setInteger("nextStop", this.nextStop);
    }

    @Override
    public List<ItemStack> step(World world) {
        if (!drill.isPowered()) return Collections.emptyList();
        drill.setWorld(world);
        List<ItemStack> drops;


        Vect3i posToDrill = this.drill.findPosToDrill();

        if (posToDrill != null) {
            drops = drill.drillAt(posToDrill);
        } else {
            steer();
            if (!drill.isPathBlocked()) {

                drops = drill.drillForward();

                if (!drill.isBlockTypeOnPath(Blocks.gravel, Blocks.sand, Blocks.bedrock)) {
                    drill.moveForward();
                    this.step++;
                }
            } else {
                if (drill.isPowered()) {
                    Minecraft minecraft = Minecraft.getMinecraft();
                    EntityClientPlayerMP thePlayer = minecraft.thePlayer;
                    if (thePlayer!=null) {
                        thePlayer.sendChatMessage("Bedrock found. Powering off");
                    }
                    drill.turnOff(world);
                }
                drops= Collections.emptyList();
            }
        }

        return drops;
    }

    private void steer() {
        if (row > 0) {
            if (step == nextStop) {
                if (row % 2 == 0) {
                    drill.turnRight();
                } else {
                    drill.turnLeft();
                }
            } else if (step == nextStop + 3) {
                if (row % 2 == 0) {
                    drill.turnRight();
                } else {
                    drill.turnLeft();
                }
                row++;
                nextStop = calculateNextStop(row);
            }
        } else if (step == 3) {
            drill.turnRight();
            step = 0;
            row = 1;
            nextStop = calculateNextStop(row);
        }
    }



    /**
     * The formula is
     * R*(R*S+3) - (S * (R * (R-1)/2)) - 3
     * Where R is the row and S is the step
     */
    private int calculateNextStop(int row) {
        int factor = (row * STEP) + 3;
        int triangular = row * (row - 1) / 2;
        return (factor * row) - (STEP * triangular) - 3;
    }
}

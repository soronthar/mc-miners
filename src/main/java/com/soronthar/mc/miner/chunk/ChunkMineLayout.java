package com.soronthar.mc.miner.chunk;

import com.soronthar.mc.core.Facing;
import com.soronthar.mc.miner.mining.BaseMineStrategy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

//TODO: find the nearest chunk
public class ChunkMineLayout extends BaseMineStrategy {
    private static final int STEP=3;
    private static final int LEFT=0;
    private static final int RIGHT=1;
    public static final int TURNING = 1;
    public static final int DRILLING = 0;
    public static final int MOVING_DOWN = 2;

    int limit = STEP;
    int phase = 0;
    int lastFacing=Facing.NORTH;
    int lastTurn=RIGHT;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound, String suffix) {
        super.readFromNBT(tagCompound, suffix);
        this.limit = tagCompound.getInteger("limit");
        this.phase = tagCompound.getInteger("phase");
        this.lastFacing = tagCompound.getInteger("lastFacing");
        this.lastTurn = tagCompound.getInteger("lastTurn");

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound, String suffix) {
        super.writeToNBT(tagCompound, suffix);
        tagCompound.setInteger("limit", this.limit);
        tagCompound.setInteger("phase", this.phase);
        tagCompound.setInteger("lastFacing", this.lastFacing);
        tagCompound.setInteger("lastTurn", this.lastTurn);
    }


    @Override
    public void init(int xCoord, int yCoord, int zCoord, int facing) {
        super.init(xCoord, yCoord, zCoord, facing);
        this.lastFacing=this.drill.facing;
        this.lastTurn=RIGHT;
    }

    //TODO: remove duplication
    @Override
    public List<ItemStack> step(World world) {
        if (!drill.isPowered()) return Collections.emptyList();
        List<ItemStack> drops;
        this.drill.setWorld(world);

        if (!this.drill.isPathBlocked()) {
            drops = this.drill.drillForward();

            if (!this.drill.isBlockTypeOnPath(Blocks.gravel, Blocks.sand, Blocks.bedrock)) {  //TODO: or facing down
                this.drill.moveForward();
                this.step++;

                if (this.phase == TURNING) {
                    turnDrill();
                    this.phase= DRILLING;
                } else if (this.phase == MOVING_DOWN) {
                    this.drill.facing=this.lastFacing;
                    turnDrill();
                    flipLastTurn();
                    this.limit=STEP;
                    this.phase=DRILLING;
                } else if (this.step % STEP == 0) {
                    this.lastFacing=this.drill.facing;
                    flipLastTurn();
                    turnDrill();
                    this.step = 0;
                    this.limit--;
                    if (this.limit == 0) {
                        this.phase = MOVING_DOWN;
                        flipLastTurn();
                        this.drill.turn(Facing.DOWN);
                    } else {
                        this.phase=TURNING;
                    }
                }
            }
        } else {
            if (drill.isPowered()) {
                messageTurnOffDrill("Bedrock found. Powering off");
                drill.turnOff(world);
            }
            drops = Collections.emptyList();
        }

        return drops;
    }

    private void flipLastTurn() {
        this.lastTurn=(this.lastTurn+1)%2;
    }

    private void turnDrill() {
        if (this.lastTurn==LEFT) {
            this.drill.turnLeft();
        } else {
            this.drill.turnRight();
        }
    }

    private void messageTurnOffDrill(String s) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP thePlayer = minecraft.thePlayer;
        if (thePlayer != null) {
            thePlayer.sendChatMessage(s);
        }
    }


}

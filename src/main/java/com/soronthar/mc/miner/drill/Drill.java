package com.soronthar.mc.miner.drill;

import com.soronthar.mc.core.Facing;
import com.soronthar.mc.core.Vect3i;
import com.soronthar.mc.miner.MinerMod;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Drill {
    public int facing = 0;
    public Vect3i drillPos = new Vect3i(0, 0, 0);
    World world;

    private boolean powered=true;


    public void readFromNBT(NBTTagCompound tagCompound) {
        this.facing = tagCompound.getInteger("facing");
        this.powered = tagCompound.getBoolean("drillpowered");
        this.drillPos = Vect3i.readFromNBT(tagCompound, "drillingPos");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("facing", this.facing);
        tagCompound.setBoolean("drillpowered", this.powered);

        this.drillPos.writeToNBT(tagCompound, "drillingPos");

    }

    public List<ItemStack> drillForward() {
        Vect3i drillPos = this.drillPos.front(this.facing);
        List<ItemStack> drops = drillAt(drillPos);
        if (this.facing!=Facing.UP && this.facing!=Facing.DOWN ) {
            drops.addAll(drillAt(drillPos.up()));
        }
        return drops;
    }

    public void turnLeft() {
        this.facing= Facing.turnLeft(this.facing);
    }

    public void turnRight() {
        this.facing=Facing.turnRight(this.facing);
    }

    public void turn(int facing) {
        this.facing=facing;
    }


    /*
* TODO:
*     * Water
*     * Lava
*/
    public List<ItemStack> drillAt(Vect3i pos) {
        int x = pos.x;
        int y = pos.y;
        int z = pos.z;

        Block block = this.world.getBlock(x, y, z);

        List<ItemStack> drops = new ArrayList<ItemStack>();

        if (!block.isAir(this.world, x, y, z) && !Block.isEqualTo(block, Blocks.bedrock)) {
            int metadata = this.world.getBlockMetadata(x, y, z);
            drops.addAll(block.getDrops(this.world, x, y, z, metadata, 0));
            world.setBlockToAir(x, y, z);
        }

        return drops;

    }

    public void moveForward() {
        removeDrillBlock(this.world);
        this.drillPos.move(this.facing);
        //todo> solve the rendering of drillblock
        Block block = GameData.blockRegistry.getObject(MinerMod.MODID+":"+DrillBlock.ID);

        this.world.setBlock(this.drillPos.x,this.drillPos.y,this.drillPos.z, block,facing,3);
        this.world.markBlockForUpdate(this.drillPos.x,this.drillPos.y,this.drillPos.z);
    }

    public void removeDrillBlock(World world) {
        Block currentBlock= world.getBlock(this.drillPos.x, this.drillPos.y, this.drillPos.z);
        if (currentBlock instanceof DrillBlock) {
            world.setBlock(this.drillPos.x,this.drillPos.y,this.drillPos.z, Blocks.air);
        }
    }

    public boolean isPowered() {
        return powered;
    }

    public void turnOff(World world) {
        this.powered=false;
        removeDrillBlock(world);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

}

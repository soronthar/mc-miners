package com.soronthar.mc.miner.mining;

import com.soronthar.mc.core.Facing;
import com.soronthar.mc.core.Vect3i;
import com.soronthar.mc.miner.xmine.XMinerBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class MiningSensor {
    private static final Block[] fillBlock = {
            Blocks.dirt,
            Blocks.grass,
            Blocks.farmland,
            Blocks.mycelium,
            Blocks.snow,
            Blocks.stone,
            Blocks.sandstone,
            Blocks.sand,
            Blocks.cobblestone,
            Blocks.netherrack,
            Blocks.bedrock,
            Blocks.soul_sand,
            Blocks.gravel,
            Blocks.air,
            Blocks.water,
            Blocks.lava};

    static int DRILL = 0, FRONT = 1; //Z
    static int GROUND = 0, BASE = 1, TOP = 2, CEIL = 3; //Y
    static int LEFT = 0, CENTER = 1, RIGHT = 2;//X
    static int MAX_Z=2, MAX_Y=4, MAX_X=3;

    Vect3i[][][] coords;
    boolean[][][] visited;

    public MiningSensor() {
        moveTo(new Vect3i(0, 0, 0), Facing.NORTH);
    }

    public void moveTo(Vect3i pos, int facing) {
        visited = new boolean[MAX_Z][MAX_Y][MAX_X];
        visited[DRILL][BASE][CENTER] = true;
        visited[DRILL][TOP][CENTER] = true;

        coords = new Vect3i[MAX_Z][MAX_Y][MAX_X];
        populateBlock(pos, DRILL, facing);
        populateBlock(pos.front(facing), FRONT, facing);
    }


    private void populateBlock(Vect3i basePos, int blockIndex, int facing) {
        populateLayer(basePos.down(), GROUND, blockIndex, facing);
        populateLayer(basePos, BASE, blockIndex, facing);

        Vect3i top = basePos.up();
        populateLayer(top, TOP, blockIndex, facing);
        populateLayer(top.up(), CEIL, blockIndex, facing);
    }

    private void populateLayer(Vect3i layerBase, int layerIndex, int blockIndex, int facing) {
        coords[blockIndex][layerIndex][LEFT] = layerBase.left(facing);
        coords[blockIndex][layerIndex][CENTER] = layerBase;
        coords[blockIndex][layerIndex][RIGHT] = layerBase.right(facing);
    }


    public Vect3i findPosToDrill(World world) {
        Vect3i result = null;
        for (int i = 0; i < MAX_Z && result == null; i++) {
            for (int j = 0; j < MAX_Y && result == null; j++) {
                for (int k = 0; k < MAX_X && result == null; k++) {
                    if (!visited[i][j][k]) {
                        visited[i][j][k] = true;
                        Vect3i blockPos = coords[i][j][k];
                        if (!isFill(world, blockPos)) {
                            result = blockPos;
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean isFill(World world, Vect3i blockPos) {
        Block blockLeft = world.getBlock(blockPos.x, blockPos.y, blockPos.z);
        boolean isFill = blockLeft.isAir(world, blockPos.x, blockPos.y, blockPos.z);
        for (int i = 0; i < fillBlock.length && !isFill; i++) {
            Block fill = fillBlock[i];
            isFill = Block.isEqualTo(fill, blockLeft);
        }
        return isFill;
    }


    public void writeToNBT(NBTTagCompound tagCompound, String prefix) {
        NBTTagList explorationData = new NBTTagList();

        for (int i = 0; i < MAX_Z; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                for (int k = 0; k < MAX_X; k++) {
                    NBTTagCompound item = new NBTTagCompound();
                    item.setIntArray("index", new int[]{i, j, k});
                    item.setBoolean("visited", visited[i][j][k]);
                    Vect3i pos = coords[i][j][k];
                    pos.writeToNBT(item, "coords");
                    explorationData.appendTag(item);
                }
            }
        }
        tagCompound.setTag(prefix, explorationData);
    }

    public void readFromNBT(NBTTagCompound tagCompound, String prefix) {
        NBTTagList list = tagCompound.getTagList(prefix, 10);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound item = list.getCompoundTagAt(i);
            int[] idx = item.getIntArray("index");
            visited[idx[0]][idx[1]][idx[2]] = item.getBoolean("visited");
            coords[idx[0]][idx[1]][idx[2]] = Vect3i.readFromNBT(item, "coords");
        }
    }


    /**
     * Checks if in the mining path there are blocks specific kind of blocks.
     * If checks the column in front of the current position, from BASE to CEIL.
     * @param world the world
     * @param blocks blocks to check
     * @return true if the specified kind of blocks are in the path, false otherwise
     */
    public boolean isBlockTypeOnPath(World world, Block... blocks) {
        boolean result = false;
        for (int i = BASE; i < MAX_Y && !result; i++) {
            Vect3i coordToCheck=coords[FRONT][i][CENTER];
            Block blockToCheck=world.getBlock(coordToCheck.x,coordToCheck.y,coordToCheck.z);
            for (int j = 0; j < blocks.length && !result; j++) {
                Block referenceBlock = blocks[j];
                result = Block.isEqualTo(referenceBlock, blockToCheck);
            }
        }

        return result;

    }

    public boolean isPathBlocked(World world) {
        return isBlockTypeOnPath(world,Blocks.bedrock, XMinerBlock.instance);
    }

}

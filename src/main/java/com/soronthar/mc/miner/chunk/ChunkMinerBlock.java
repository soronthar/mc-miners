package com.soronthar.mc.miner.chunk;


import com.soronthar.mc.miner.MinerBlock;
import com.soronthar.mc.miner.MinerMod;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ChunkMinerBlock extends MinerBlock {

    public static final String ID = "chunkminer";
    public static Block instance=new ChunkMinerBlock();


    public ChunkMinerBlock() {
        super(MinerMod.MODID, ID);
    }


    public TileEntity createNewTileEntity(World world, int meta) {
        return new ChunkMinerEntity(meta);
    }

}

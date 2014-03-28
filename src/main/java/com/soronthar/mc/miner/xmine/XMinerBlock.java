package com.soronthar.mc.miner.xmine;


import com.soronthar.mc.miner.MinerBlock;
import com.soronthar.mc.miner.MinerMod;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class XMinerBlock extends MinerBlock {

    public static final String ID = "xminer";
    public static Block instance=new XMinerBlock();


    public XMinerBlock() {
        super(MinerMod.MODID, ID);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return new XMinerEntity(meta);
    }

}

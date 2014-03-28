package com.soronthar.mc.miner.drill;

import com.soronthar.mc.core.BaseBlockContainer;
import com.soronthar.mc.miner.MinerMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

//TODO: fix facing when drilling
public class DrillBlock extends BaseBlockContainer {
    public static final String ID = "drill";
    public static DrillBlock instance = new DrillBlock();


    public DrillBlock() {
        super(MinerMod.MODID, ID, Material.rock);
        this.setBlockUnbreakable();
        this.setStepSound(Block.soundTypeMetal);
        this.setLightLevel(1.0F);
    }

    protected void setDefaultDirection(World world, int x, int y, int z) {
    }
}

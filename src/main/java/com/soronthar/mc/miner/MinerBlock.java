package com.soronthar.mc.miner;


import com.soronthar.mc.core.BaseBlockContainer;
import com.soronthar.mc.core.Facing;
import com.soronthar.mc.core.HarvestLevel;
import com.soronthar.mc.core.HarvestTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class MinerBlock extends BaseBlockContainer {

    public MinerBlock(String modId, String blockName) {
        super(modId, blockName, Material.iron);
        this.setHardness(0.5F)
                .setStepSound(Block.soundTypeMetal)
                .setCreativeTab(CreativeTabs.tabRedstone);
        this.setHarvestLevel(HarvestTool.PICKAXE, HarvestLevel.STONE);
    }


    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(MinerMod.instance, 0, world, x, y, z);
        }
        return true;
    }

    protected void setFacing(World world, int x, int y, int z, byte facing) {
        world.setBlockMetadataWithNotify(x, y, z, Facing.opossite(facing), 2);
    }

}

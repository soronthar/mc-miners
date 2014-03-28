package com.soronthar.mc.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
//TODO: extract icon management
public abstract class BaseBlockContainer extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconFace;
    @SideOnly(Side.CLIENT)
    private IIcon iconBack;

    private String id;
    private final String modId;


    public BaseBlockContainer(String modId, String blockName, Material material) {
        super(material);
        this.id=blockName;
        this.setBlockName(this.id);
        this.modId=modId;
    }

    public String getId() {
        return id;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int facing) {
        if(side == 0 || side == 1) {
            return this.iconTop;
        } else if(side == Facing.opossite(facing)) {
            return this.iconBack;
        } else if(side == facing) {
            return this.iconFace;
        } else {
            return this.iconSide;
        }
    }

    //todo> optimize for those blocks that repeat textures
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.iconSide = getIcon(register, this.id, "side");
        this.iconTop = getIcon(register, this.id, "top");
        this.iconFace = getIcon(register, this.id, "face");
        this.iconBack = getIcon(register, this.id, "back");
    }

    private IIcon getIcon(IIconRegister register, String preffix, String side) {
        return register.registerIcon(this.modId + ":"+preffix+"_"+side);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return null;
    }

    public void setHarvestLevel(HarvestTool tool, HarvestLevel level) {
        this.setHarvestLevel(tool.getTool(),level.getLevel());
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity entity=  world.getTileEntity(x, y, z);
        if (entity!=null && entity instanceof InventoryEntity) {
            ((InventoryEntity)entity).dropContent();
        }
        world.removeTileEntity(x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
        world.removeTileEntity(x, y, z);
    }

    @Override
    public boolean onBlockEventReceived(World world, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_) {
        boolean result =super.onBlockEventReceived(world, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = world.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) &&result : result ;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metaData) {
        return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, metaData);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        setDefaultDirection(world, x, y, z);
    }

    protected void setDefaultDirection(World world, int x, int y, int z) {
        if(!world.isRemote) {
            Block blockNorth = world.getBlock(x, y, z - 1);
            Block blockSouth = world.getBlock(x, y, z + 1);
            Block blockWest = world.getBlock(x - 1, y, z);
            Block blockEast = world.getBlock(x + 1, y, z);
            byte meta = Facing.SOUTH;

            if(blockWest.isOpaqueCube() && !blockEast.isOpaqueCube()) meta = Facing.EAST;
            if(blockEast.isOpaqueCube() && !blockWest.isOpaqueCube()) meta = Facing.WEST;
            if(blockNorth.isOpaqueCube() && !blockSouth.isOpaqueCube()) meta = Facing.SOUTH;
            if(blockSouth.isOpaqueCube() && !blockNorth.isOpaqueCube()) meta = Facing.NORTH;

            setFacing(world,x, y, z, meta);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        int rotation = MathHelper.floor_double((double) (entity.rotationYaw * 4F / 360F) + 0.5D) & 3;
        setFacing(world, x, y, z, Facing.rotationToFacing(rotation));

    }

    protected void setFacing(World world, int x, int y, int z, byte facing) {
        world.setBlockMetadataWithNotify(x, y, z, facing, 2);
    }


}

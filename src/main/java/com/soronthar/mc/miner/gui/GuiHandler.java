package com.soronthar.mc.miner.gui;

import com.soronthar.mc.miner.MinerEntity;
import com.soronthar.mc.miner.MinerMod;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(MinerMod.instance, this);
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(x, y, z);

        switch (id) {
            case 0:
                if (entity != null && entity instanceof MinerEntity) {
                    return new MinerContainer(player.inventory, (MinerEntity) entity);
                } else {
                    return null;
                }
            default:
                return null;
        }
    }


    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(x, y, z);

        switch (id) {
            case 0:
                if (entity != null && entity instanceof MinerEntity) {
                    return new GuiMiner(player.inventory, (MinerEntity) entity);
                } else {
                    return null;
                }
            default:
                return null;
        }
    }
}


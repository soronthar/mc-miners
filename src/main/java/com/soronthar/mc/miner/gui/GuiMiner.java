package com.soronthar.mc.miner.gui;

import com.soronthar.mc.miner.MinerEntity;
import com.soronthar.mc.miner.MinerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiMiner extends GuiContainer {
    public static final ResourceLocation texture = new ResourceLocation(MinerMod.MODID, "textures/gui/miner.png");

    public GuiMiner(InventoryPlayer invPlayer, MinerEntity entity) {
        super(new MinerContainer(invPlayer, entity));
        xSize = 176;
        ySize = 165;

    }


    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft,guiTop,0,0,xSize,ySize);
    }

}

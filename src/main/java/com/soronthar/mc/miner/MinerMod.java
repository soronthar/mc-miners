package com.soronthar.mc.miner;

import com.soronthar.mc.miner.chunk.ChunkMinerBlock;
import com.soronthar.mc.miner.chunk.ChunkMinerEntity;
import com.soronthar.mc.miner.drill.DrillBlock;
import com.soronthar.mc.miner.gui.GuiHandler;
import com.soronthar.mc.miner.xmine.XMinerBlock;
import com.soronthar.mc.miner.xmine.XMinerEntity;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
TODO:
   * Remove the prefix from the block names
   * handle water and lava
   * build main corridor
   * Fuel
   * place torches
   * detect caves
   * Recipe

   * Types of miners:
      * Basic miner: stops on caves, water and lava. No corridors
      * Advanced miner: Fill up gaps when mining, place torches, corridors
      * Obsidian Farmer: when lava is found, mine obsidian
      * Automated Quarry: start mining on "surface", mine 5x5 chunks, then move 2 levels down.
      * Store Fluids

 */

@Mod(modid = MinerMod.MODID, version = MinerMod.VERSION)
public class MinerMod {
    public static final String MODID = "sorontharminer";
    public static final String VERSION = "0.1";
    public static final Logger log = LogManager.getFormatterLogger(MODID);


    @Mod.Instance(MODID)
    public static MinerMod instance;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        GameRegistry.registerBlock(XMinerBlock.instance, XMinerBlock.ID);
        GameRegistry.registerTileEntity(XMinerEntity.class, XMinerEntity.ID);

        GameRegistry.registerBlock(ChunkMinerBlock.instance, ChunkMinerBlock.ID);
        GameRegistry.registerTileEntity(ChunkMinerEntity.class, ChunkMinerEntity.ID);

        GameRegistry.registerBlock(DrillBlock.instance, DrillBlock.ID);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        new GuiHandler();
    }

}

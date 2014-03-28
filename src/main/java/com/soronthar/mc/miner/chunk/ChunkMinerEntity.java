package com.soronthar.mc.miner.chunk;

import com.soronthar.mc.miner.MinerEntity;
import com.soronthar.mc.miner.mining.MiningStrategy;

public class ChunkMinerEntity extends MinerEntity {

    public static final String ID = "chunkminerentity";

    public ChunkMinerEntity() {
        super();
    }

    public ChunkMinerEntity(int facing) {
        super(facing);
    }

    @Override
    protected void init() {
        super.init();
        this.ticksPerStep=5;
    }

    @Override
    protected MiningStrategy createStrategy() {
        return new ChunkMineLayout();
    }
}

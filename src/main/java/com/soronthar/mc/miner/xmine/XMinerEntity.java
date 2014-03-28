package com.soronthar.mc.miner.xmine;

import com.soronthar.mc.miner.MinerEntity;
import com.soronthar.mc.miner.mining.MiningStrategy;

public class XMinerEntity extends MinerEntity {

    public static final String ID = "xminerentity";

    public XMinerEntity() {
        super();
    }

    public XMinerEntity(int facing) {
        super(facing);
    }

    @Override
    protected MiningStrategy createStrategy() {
        return new XMineLayout();
    }
}

package com.soronthar.mc.core;

import net.minecraft.nbt.NBTTagCompound;

import javax.vecmath.Tuple3i;

public class Vect3i extends Tuple3i{
    public static final Vect3i[] DIRECTIONS = {
            new Vect3i( 0,-1, 0), //DOWN
            new Vect3i( 0, 1, 0), //UP
            new Vect3i( 0, 0,-1), //NORTH
            new Vect3i( 0, 0, 1), //SOUTH
            new Vect3i(-1, 0, 0), //WEST
            new Vect3i( 1, 0, 0)  //EAST
    };


    public Vect3i(int i, int i1, int i2) {
        super(i, i1, i2);
    }


    public Vect3i up() {
        return getFacingDirection(Facing.UP);
    }

    public Vect3i down() {
        return getFacingDirection(Facing.DOWN);
    }

    public Vect3i left(int facing) {
        return getFacingDirection(Facing.turnLeft(facing));
    }

    public Vect3i right(int facing) {
        return getFacingDirection(Facing.turnRight(facing));
    }


    public Vect3i front(int facing) {
        return getFacingDirection(facing);
    }

    public Vect3i back(int facing) {
        return getFacingDirection(Facing.opossite(facing));
    }

    public Vect3i getFacingDirection(int facing) {
        Vect3i result = new Vect3i(0, 0, 0);
        result.add(DIRECTIONS[facing], this);
        return result;
    }

    public void move(int facing) {
        this.add(DIRECTIONS[facing]);
    }

    public static Vect3i readFromNBT(NBTTagCompound tagCompound, String prefix) {
        int x = tagCompound.getInteger(prefix+"X");
        int y = tagCompound.getInteger(prefix+"Y");
        int z = tagCompound.getInteger(prefix+"Z");

        return new Vect3i(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tagCompound, String prefix) {
        tagCompound.setInteger(prefix+"X", this.x);
        tagCompound.setInteger(prefix+"Y", this.y);
        tagCompound.setInteger(prefix+"Z", this.z);
    }
}


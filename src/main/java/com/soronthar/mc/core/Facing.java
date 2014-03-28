package com.soronthar.mc.core;

public class Facing {
    public static byte DOWN=0;
    public static byte UP=1;
    public static byte NORTH=2;
    public static byte SOUTH=3;
    public static byte WEST=4;
    public static byte EAST=5;

    private static byte[] opossites={1,0,3,2,5,4};
    private static byte[] rotationToFacing={NORTH,EAST,SOUTH,WEST};
    private static int[] right={5,4,5,4,2,3};
    private static int[] left={4,5,4,5,3,2};

    public static byte opossite(int facing) {
        return opossites[facing];
    }

    public static byte rotationToFacing(int rotation) {
        return rotationToFacing[rotation];
    }

    public static int turnRight(int facing) {
        return right[facing];
    }

    public static int turnLeft(int facing) {
        return left[facing];
    }
}

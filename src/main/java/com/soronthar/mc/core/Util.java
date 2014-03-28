package com.soronthar.mc.core;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rafael Alvarez
 * Date: 3/23/14
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static void eject(ItemStack itemstack, World world, float xCoord, float yCoord, float zCoord) {
        if (itemstack != null) {
            Random random = new Random();

            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            float f3 = 0.05F;
            int i1 = itemstack.stackSize;
            EntityItem entityitem = new EntityItem(world, xCoord + f, yCoord + 1 + f1, zCoord + f2,
                    new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
            entityitem.motionX = (float) random.nextGaussian() * f3;
            entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = (float) random.nextGaussian() * f3;
            if (itemstack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
            }
            world.spawnEntityInWorld(entityitem);
        }
    }
}

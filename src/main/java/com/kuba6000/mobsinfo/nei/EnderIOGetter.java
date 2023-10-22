package com.kuba6000.mobsinfo.nei;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.mixin.EnderIO.BlockPoweredSpawnerAccessor;

import crazypants.enderio.EnderIO;
import crazypants.enderio.machine.spawner.BlockPoweredSpawner;

class EnderIOGetter {

    public static String getContainedMobOrNull(ItemStack stack) {
        String mob = BlockPoweredSpawner.getSpawnerTypeFromItemStack(stack);
        if (mob != null) return mob;
        return EnderIO.itemSoulVessel.getMobTypeFromStack(stack);
    }

    public static Block blockPoweredSpawner() {
        return EnderIO.blockPoweredSpawner;
    }

    public static Item itemSoulVessel() {
        return EnderIO.itemSoulVessel;
    }

    public static ItemStack BlockPoweredSpawner$createItemStackForMob(String type) {
        return ((BlockPoweredSpawnerAccessor) EnderIO.blockPoweredSpawner).callCreateItemStackForMob(type);
    }

    public static ItemStack ItemSoulVessel$createVesselWithEntityStub(String entityId) {
        return EnderIO.itemSoulVessel.createVesselWithEntityStub(entityId);
    }
}

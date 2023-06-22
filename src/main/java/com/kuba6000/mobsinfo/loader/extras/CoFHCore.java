package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import cofh.core.entity.DropHandler;

public class CoFHCore implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        double chanceMod = 0.975d;
        if (!DropHandler.mobPvEOnly) {
            if (recipe.entity instanceof EntitySkeleton) {
                if (((EntitySkeleton) recipe.entity).getSkeletonType() == 0 && DropHandler.skeletonEnabled) {
                    if (DropHandler.skeletonChance != 0) {
                        MobDrop drop = new MobDrop(
                            new ItemStack(Items.skull, 1, 0),
                            MobDrop.DropType.Normal,
                            (int) ((double) DropHandler.skeletonChance * 100d * chanceMod),
                            null,
                            null,
                            false,
                            false);
                        drops.add(drop);
                    }
                } else if (DropHandler.witherSkeletonChance != 0) {
                    if (((EntitySkeleton) recipe.entity).getSkeletonType() == 1 && DropHandler.witherSkeletonEnabled) {
                        MobDrop drop = new MobDrop(
                            new ItemStack(Items.skull, 1, 1),
                            MobDrop.DropType.Normal,
                            (int) ((double) DropHandler.witherSkeletonChance * 100d * chanceMod),
                            null,
                            null,
                            false,
                            false);
                        drops.add(drop);
                    }
                }
            } else if (recipe.entity instanceof EntityZombie && DropHandler.zombieEnabled) {
                if (DropHandler.zombieChance != 0) {
                    MobDrop drop = new MobDrop(
                        new ItemStack(Items.skull, 1, 2),
                        MobDrop.DropType.Normal,
                        (int) ((double) DropHandler.zombieChance * 100d * chanceMod),
                        null,
                        null,
                        false,
                        false);
                    drops.add(drop);
                }
            } else if (recipe.entity instanceof EntityCreeper && DropHandler.creeperEnabled) {
                if (DropHandler.creeperChance != 0) {
                    MobDrop drop = new MobDrop(
                        new ItemStack(Items.skull, 1, 4),
                        MobDrop.DropType.Normal,
                        (int) ((double) DropHandler.creeperChance * 100d * chanceMod),
                        null,
                        null,
                        false,
                        false);
                    drops.add(drop);
                }
            }
        }
    }
}

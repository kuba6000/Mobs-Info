package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.items.ForbiddenItems;
import thaumcraft.api.entities.ITaintedMob;

public class ForbiddenMagic implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        // fox.spiteful.forbidden.FMEventHandler.onLivingDrops

        if (recipe.entity instanceof ITaintedMob) {
            MobDrop drop = new MobDrop(
                new ItemStack(ForbiddenItems.resource, 1, 3),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo.addAll(
                Arrays.asList(
                    "Chance: 50%",
                    "* Drops only when the mob has weakness (II or higher) potion effect applied"));
            drops.add(drop);
        }

        if (Config.silverfishEmeralds && recipe.entity instanceof EntitySilverfish) {
            MobDrop drop = new MobDrop(
                new ItemStack(ForbiddenItems.resource, 1, 0),
                MobDrop.DropType.Normal,
                286,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo.add("Chance: " + 2.86d + "% (or 10% when on Extreme Hills biome)");
            drops.add(drop);
        }

        if (Config.greedyEnch && (recipe.entity instanceof EntityVillager || recipe.entity instanceof IMob)) {
            MobDrop drop;
            if (recipe.entity instanceof EntityVillager) {
                drop = new MobDrop(
                    new ItemStack(Items.emerald, 1),
                    MobDrop.DropType.Normal,
                    0,
                    null,
                    null,
                    false,
                    false);
                drop.variableChance = true;
                drop.variableChanceInfo
                    .addAll(Arrays.asList("Chance: 100%", "* Drops only when Greedy enchantment is applied"));
            } else {
                drop = new MobDrop(
                    new ItemStack(ForbiddenItems.resource, 1, 0),
                    MobDrop.DropType.Normal,
                    0,
                    null,
                    null,
                    false,
                    false);
                drop.variableChance = true;
                drop.variableChanceInfo
                    .addAll(Arrays.asList("Chance: " + 8.57d + "%", "* Drops only when Greedy enchantment is applied"));
            }
            drops.add(drop);
        }

        if (recipe.entity.getClass() == EntitySkeleton.class) {
            MobDrop drop = new MobDrop(
                new ItemStack(Items.skull, 1, ((EntitySkeleton) recipe.entity).getSkeletonType()),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo
                .addAll(Arrays.asList("Chance: " + 15.38d + "%", "* Drops only when killed using Skull Axe"));
            drops.add(drop);
        }

        if (recipe.entity.getClass() == EntityZombie.class) {
            MobDrop drop = new MobDrop(
                new ItemStack(Items.skull, 1, 2),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo
                .addAll(Arrays.asList("Chance: " + 11.54d + "%", "* Drops only when killed using Skull Axe"));
            drops.add(drop);
        }
        if (recipe.entity.getClass() == EntityCreeper.class) {
            MobDrop drop = new MobDrop(
                new ItemStack(Items.skull, 1, 4),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo
                .addAll(Arrays.asList("Chance: " + 11.54d + "%", "* Drops only when killed using Skull Axe"));
            drops.add(drop);
        }

        MobDrop drop = new MobDrop(
            new ItemStack(ForbiddenItems.deadlyShards, 1, 5),
            MobDrop.DropType.Normal,
            (int) (MobDrop.getChanceBasedOnFromTo(1, 3) * 10000d * 0.1333333d),
            null,
            null,
            false,
            false);
        drop.clampChance();
        drop.variableChance = true;
        drop.variableChanceInfo.addAll(
            Arrays.asList(
                "Chance: " + ((double) drop.chance / 100d) + "%",
                "* Drops only when killed in the NETHER dimension",
                "* Drops only when killed by non player entity"));
        drop.chance = 0;
        drops.add(drop);

        if (recipe.entity instanceof IMob) {
            MobDrop drop2 = new MobDrop(
                new ItemStack(ForbiddenItems.deadlyShards, 1, 0),
                MobDrop.DropType.Normal,
                328,
                null,
                null,
                false,
                false);
            drops.add(drop2);

            if (recipe.entity instanceof IBossDisplayData) {
                MobDrop drop3 = new MobDrop(
                    new ItemStack(ForbiddenItems.deadlyShards, 2, 3),
                    MobDrop.DropType.Normal,
                    10000,
                    null,
                    null,
                    false,
                    false);
                drops.add(drop3);
            }

            MobDrop drop3 = new MobDrop(
                new ItemStack(ForbiddenItems.deadlyShards, 1, 6),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop3.variableChance = true;
            drop3.variableChanceInfo.addAll(
                Arrays.asList(
                    "Chance: 0%",
                    "* Each level of looting gives +5%",
                    "* Each level of treasure focus on wand gives +5%"));
            drops.add(drop3);
        }
    }
}

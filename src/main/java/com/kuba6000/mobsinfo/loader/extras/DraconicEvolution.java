package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.entity.EntityCustomDragon;
import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.handler.MinecraftForgeEventHandler;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;
import com.kuba6000.mobsinfo.mixin.DraconicEvolution.MinecraftForgeEventHandlerAccessor;

public class DraconicEvolution implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntityDragon || k.equals("HardcoreEnderExpansion.Dragon")) {
            MobDrop drop = new MobDrop(
                new ItemStack(ModItems.dragonHeart, recipe.entity instanceof EntityCustomDragon ? 2 : 1),
                MobDrop.DropType.Normal,
                10000,
                null,
                null,
                false,
                false);
            drops.add(drop);
            recipe.mOutputs.add(drop);

            MobDrop drop2 = new MobDrop(
                new ItemStack(ModItems.draconiumDust),
                MobDrop.DropType.Normal,
                (int) (MobDrop.getChanceBasedOnFromTo(30, 30 + 29) * 10000d),
                null,
                null,
                false,
                false);
            drop2.clampChance();

            drops.add(drop2);
            recipe.mOutputs.add(drop2);

        }

        if (!((MinecraftForgeEventHandlerAccessor) (new MinecraftForgeEventHandler())).callIsValidEntity(recipe.entity))
            return;

        ItemStack soul = new ItemStack(ModItems.mobSoul);
        soul.stackTagCompound = new NBTTagCompound();
        soul.stackTagCompound.setString("Name", k);
        if (recipe.entity instanceof EntitySkeleton)
            soul.stackTagCompound.setInteger("SkeletonType", ((EntitySkeleton) recipe.entity).getSkeletonType());

        MobDrop drop = new MobDrop(soul, MobDrop.DropType.Normal, 0, null, null, false, false);
        drop.variableChance = true;

        double baseChance = (1d / (recipe.entity instanceof EntityAnimal ? ConfigHandler.passiveSoulDropChance
            : ConfigHandler.soulDropChance)) * 100d;

        drop.variableChanceInfo.addAll(
            Arrays.asList(
                "Variable chance:",
                "BASED ON REAPER ENCHANT LEVEL - AND WEAPON |",
                "LEVEL | NONE | I | II | III | IV | V",
                String.format(
                    "Any/None | 0%% | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%%",
                    baseChance * 1,
                    baseChance * 2,
                    baseChance * 3,
                    baseChance * 4,
                    baseChance * 5),
                String.format(
                    "Wyvern Sword/Bow | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%%",
                    baseChance * 1,
                    baseChance * 2,
                    baseChance * 3,
                    baseChance * 4,
                    baseChance * 5,
                    baseChance * 6),
                String.format(
                    "Draconic Sword/Bow | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%%",
                    baseChance * 2,
                    baseChance * 3,
                    baseChance * 4,
                    baseChance * 5,
                    baseChance * 6,
                    baseChance * 7),
                String.format(
                    "Draconic Staff of Power | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%% | %.2f%%",
                    baseChance * 3,
                    baseChance * 4,
                    baseChance * 5,
                    baseChance * 6,
                    baseChance * 7,
                    baseChance * 8)));

        drops.add(drop);
        recipe.mOutputs.add(drop);
    }
}

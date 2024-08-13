package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import emt.init.EMTItems;
import thaumcraft.common.entities.monster.EntityTaintChicken;

public class ElectroMagicTools implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntityCreeper) {
            MobDrop drop = new MobDrop(
                new ItemStack(EMTItems.itemEMTItems, 1, 6),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.chanceModifiers.addAll(Arrays.asList(new IChanceModifier.NormalChance(100d), new EMTCreeper()));
            drops.add(drop);
        }
        if (recipe.entity instanceof EntityTaintChicken) {
            MobDrop drop = new MobDrop(
                new ItemStack(EMTItems.itemEMTItems, 1, 13),
                MobDrop.DropType.Normal,
                (int) (MobDrop.getChanceBasedOnFromTo(0, 2) * 10000d),
                null,
                null,
                false,
                false);
            drop.clampChance();
            drops.add(drop);
        }
    }

    private static class EMTCreeper implements IChanceModifier {

        @Override
        public String getDescription() {
            return Translations.EMT_CREEPER.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (chance == 0d) return 0d;
            if (victim == null) return 0d;
            if (victim instanceof EntityCreeper && ((EntityCreeper) victim).getPowered()) return chance;
            return 0d;
        }

    }
}

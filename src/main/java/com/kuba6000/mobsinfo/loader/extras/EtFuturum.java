package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import ganymedes01.etfuturum.ModBlocks;
import ganymedes01.etfuturum.ModItems;
import ganymedes01.etfuturum.configuration.configs.ConfigBlocksItems;
import ganymedes01.etfuturum.configuration.configs.ConfigFunctions;

public class EtFuturum implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        // ganymedes01.etfuturum.core.handlers.ServerEventHandler.dropEvent

        // skull
        if (ConfigFunctions.enableSkullDrop) {
            ItemStack skull = null;
            if (recipe.entity.getClass() == EntityZombie.class) skull = new ItemStack(Items.skull, 1, 2);
            else if (recipe.entity.getClass() == EntitySkeleton.class
                && ((EntitySkeleton) recipe.entity).getSkeletonType() == 0) skull = new ItemStack(Items.skull, 1, 0);
            else if (recipe.entity.getClass() == EntityCreeper.class) skull = new ItemStack(Items.skull, 1, 4);

            if (skull != null) {
                MobDrop drop = new MobDrop(skull, MobDrop.DropType.Normal, 0, null, null, false, false);
                drop.variableChance = true;
                drop.chanceModifiers
                    .addAll(Arrays.asList(new NormalChance(100d), new DropsOnlyWhenKilledByPoweredCreeperModifier()));
                drops.add(drop);
            }
        }

        // mutton
        if (ModItems.MUTTON_RAW.isEnabled() && ModItems.MUTTON_COOKED.isEnabled() && recipe.entity instanceof EntitySheep) {
            MobDrop drop = new MobDrop(
                new ItemStack(ModItems.MUTTON_RAW.get()),
                MobDrop.DropType.Normal,
                (int) (MobDrop.getChanceBasedOnFromTo(1, 3) * 10000d),
                null,
                null,
                true,
                false);
            drop.clampChance();
            drops.add(drop);
        }

        // wither rose
        if (ModBlocks.WITHER_ROSE.isEnabled()) {
            MobDrop drop = new MobDrop(
                ModBlocks.WITHER_ROSE.newItemStack(1, 0),
                MobDrop.DropType.Additional,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.chanceModifiers.addAll(Arrays.asList(new NormalChance(100d), new WitherRoseModifier()));
            drops.add(drop);
        }
    }

    private static class DropsOnlyWhenKilledByPoweredCreeperModifier implements IChanceModifier {

        @Override
        public String getDescription() {
            return Translations.ETFUTURUM_POWERED_CREEPER.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return attacker instanceof EntityCreeper && ((EntityCreeper) attacker).getPowered() ? chance : 0d;
        }
    }

    private static class WitherRoseModifier implements IChanceModifier {

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void applyTooltip(List<String> currentTooltip) {
            currentTooltip.addAll(
                Arrays.asList(Translations.ETFUTURUM_WITHER_ROSE.get(), Translations.ETFUTURUM_WITHER_ROSE_1.get()));
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return attacker instanceof EntityWither ? chance : 0d;
        }
    }
}

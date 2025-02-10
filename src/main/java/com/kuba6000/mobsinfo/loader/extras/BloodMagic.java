package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;

public class BloodMagic implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (!(recipe.entity instanceof EntityAnimal)) {
            MobDrop drop = new MobDrop(
                new ItemStack(ModItems.weakBloodShard),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.chanceModifiers.addAll(
                Arrays.asList(new IChanceModifier.NormalChance(50d), new IChanceModifier.DropsOnlyWithWeaknessIII()));
            drops.add(drop);
        }
        if (recipe.entity instanceof EntityDemon) {
            for (MobDrop drop : drops) {
                if (drop.stack.getItem() == ModItems.demonPlacer) {
                    drop.variableChance = true;
                    drop.chanceModifiers
                        .addAll(Arrays.asList(new IChanceModifier.NormalChance(100d), new DemonDemonPlacer()));
                }
            }
        }
        // WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt.dropFewItems
        if (recipe.entity instanceof EntityMinorDemonGrunt) {
            ItemStack lifeShardStack = new ItemStack(ModItems.baseItems, 1, 28);
            ItemStack soulShardStack = new ItemStack(ModItems.baseItems, 1, 29);
            MobDrop drop = new MobDrop(lifeShardStack, MobDrop.DropType.Normal, 0, 0, null, true, false);
            drop.variableChance = true;
            drop.chanceModifiers
                .addAll(Arrays.asList(new IChanceModifier.NormalChance(30d), new MinorDemonGruntShards()));
            drops.add(drop);
            MobDrop drop2 = new MobDrop(soulShardStack, MobDrop.DropType.Normal, 0, 0, null, true, false);
            drop2.variableChance = true;
            drop2.chanceModifiers
                .addAll(Arrays.asList(new IChanceModifier.NormalChance(30d), new MinorDemonGruntShards()));
            drops.add(drop2);
        }
    }

    private static class MinorDemonGruntShards implements IChanceModifier {

        @Override
        public String getDescription() {
            return Translations.BLOODMAGIC_DEMON_SHARDS.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return chance;
        }
    }

    private static class DemonDemonPlacer implements IChanceModifier {

        @Override
        public String getDescription() {
            return Translations.BLOODMAGIC_DEMON_PLACER.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return 0d;
        }
    }
}

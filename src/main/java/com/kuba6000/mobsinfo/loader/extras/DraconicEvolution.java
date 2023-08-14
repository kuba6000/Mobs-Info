package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.entity.EntityCustomDragon;
import com.brandon3055.draconicevolution.common.handler.ConfigHandler;
import com.brandon3055.draconicevolution.common.handler.MinecraftForgeEventHandler;
import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;
import com.kuba6000.mobsinfo.mixin.DraconicEvolution.MinecraftForgeEventHandlerAccessor;

import io.netty.buffer.ByteBuf;

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

        }

        if (!((MinecraftForgeEventHandlerAccessor) (new MinecraftForgeEventHandler())).callIsValidEntity(recipe.entity))
            return;

        ItemStack soul = new ItemStack(ModItems.mobSoul);
        soul.stackTagCompound = new NBTTagCompound();
        soul.stackTagCompound.setString("Name", k.equals("witherSkeleton") ? "Skeleton" : k);
        if (recipe.entity instanceof EntitySkeleton)
            soul.stackTagCompound.setInteger("SkeletonType", ((EntitySkeleton) recipe.entity).getSkeletonType());

        MobDrop drop = new MobDrop(soul, MobDrop.DropType.Normal, 0, null, null, false, false);
        drop.variableChance = true;

        double baseChance = (1d / (recipe.entity instanceof EntityAnimal ? ConfigHandler.passiveSoulDropChance
            : ConfigHandler.soulDropChance)) * 100d;

        drop.chanceModifiers.add(new DraconicEvolutionSoulChanceModifier(baseChance));

        drops.add(drop);
    }

    private static class DraconicEvolutionSoulChanceModifier implements IChanceModifier {

        double baseChance;

        DraconicEvolutionSoulChanceModifier() {}

        DraconicEvolutionSoulChanceModifier(double baseChance) {
            this.baseChance = baseChance;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void applyTooltip(List<String> currentTooltip) {
            currentTooltip.addAll(
                Arrays.asList(
                    Translations.VARIABLE_CHANCE.get() + ":",
                    Translations.DRACONIC_EVOLUTION_MOB_SOUL.get(),
                    Translations.DRACONIC_EVOLUTION_MOB_SOUL_1.get(),
                    Translations.DRACONIC_EVOLUTION_MOB_SOUL_2
                        .get(0d, baseChance * 1, baseChance * 2, baseChance * 3, baseChance * 4, baseChance * 5),
                    Translations.DRACONIC_EVOLUTION_MOB_SOUL_3.get(
                        baseChance * 1,
                        baseChance * 2,
                        baseChance * 3,
                        baseChance * 4,
                        baseChance * 5,
                        baseChance * 6),
                    Translations.DRACONIC_EVOLUTION_MOB_SOUL_4.get(
                        baseChance * 2,
                        baseChance * 3,
                        baseChance * 4,
                        baseChance * 5,
                        baseChance * 6,
                        baseChance * 7),
                    Translations.DRACONIC_EVOLUTION_MOB_SOUL_5.get(
                        baseChance * 3,
                        baseChance * 4,
                        baseChance * 5,
                        baseChance * 6,
                        baseChance * 7,
                        baseChance * 8)));
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return 0d; // TODO
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {
            byteBuf.writeDouble(baseChance);
        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {
            baseChance = byteBuf.readDouble();
        }
    }
}

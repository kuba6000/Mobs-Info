package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

import com.arc.bloodarsenal.common.items.ModItems;
import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBuf;

public class BloodArsenal implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (GameRegistry.findItem(com.arc.bloodarsenal.common.BloodArsenal.MODID, "heart") != null) {
            if (recipe.entity instanceof EntityZombie) {
                MobDrop drop = new MobDrop(
                    new ItemStack(ModItems.heart),
                    MobDrop.DropType.Normal,
                    0,
                    null,
                    null,
                    false,
                    false);
                drop.variableChance = true;
                drop.chanceModifiers.addAll(Arrays.asList(new NormalChance(1d), new DropsOnlyWithWeakness()));
                drops.add(drop);
            } else if (recipe.entity instanceof EntityVillager) {
                MobDrop drop = new MobDrop(
                    new ItemStack(ModItems.heart),
                    MobDrop.DropType.Normal,
                    0,
                    null,
                    null,
                    false,
                    false);
                drop.variableChance = true;
                drop.chanceModifiers.addAll(Arrays.asList(new NormalChance(25d), new DropsOnlyWithWeakness()));
                drops.add(drop);
            }
        }
        if (GameRegistry.findItem(com.arc.bloodarsenal.common.BloodArsenal.MODID, "wolf_hide") != null) {
            if (recipe.entity instanceof EntityWolf) {
                drops.add(
                    new MobDrop(
                        new ItemStack(ModItems.wolf_hide),
                        MobDrop.DropType.Normal,
                        4000,
                        null,
                        null,
                        false,
                        false));
            }
        }
    }

    private static class DropsOnlyWithWeakness implements IChanceModifier {

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.DROPS_ONLY_WITH_WEAKNESS.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (chance == 0d) return 0d;
            if (victim == null) return 0d;
            if (victim.isPotionActive(Potion.weakness)) return chance;
            return 0d;
        }

        @Override
        public void writeToByteBuf(ByteBuf byteBuf) {

        }

        @Override
        public void readFromByteBuf(ByteBuf byteBuf) {

        }
    }
}

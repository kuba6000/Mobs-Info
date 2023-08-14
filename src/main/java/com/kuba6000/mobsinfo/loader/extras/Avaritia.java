package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBuf;

public class Avaritia implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntitySkeleton) {
            MobDrop drop = new MobDrop(
                new ItemStack(Items.skull, 1, 1),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.chanceModifiers.addAll(Arrays.asList(new NormalChance(100d), new AvaritiaSkullSwordModifier()));
            drops.add(drop);
        }
    }

    private static class AvaritiaSkullSwordModifier implements IChanceModifier {

        private static final Item skull_sword = GameRegistry.findItem("Avaritia", "Skull_Sword");

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public String getDescription() {
            return Translations.AVARITIA_SKULL_SWORD.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            if (!(attacker instanceof EntityPlayer)) return 0d;
            ItemStack weapon = ((EntityPlayer) attacker).getHeldItem();
            if (weapon != null && weapon.getItem() == skull_sword
                && drops.stream()
                    .noneMatch(i -> i.getItem() == Items.skull))
                return chance;
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

package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import openblocks.common.TrophyHandler;

public class OpenBlocks implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (openblocks.OpenBlocks.Blocks.trophy == null) return;
        TrophyHandler.Trophy trophy = TrophyHandler.Trophy.TYPES.get(k);
        if (trophy != null) {
            MobDrop drop = new MobDrop(trophy.getItemStack(), MobDrop.DropType.Normal, 0, null, null, false, false);
            drop.variableChance = true;
            drop.chanceModifiers.add(new OpenBlocksSmallChance());
            drops.add(drop);
        }
    }

    private static class OpenBlocksSmallChance implements IChanceModifier {

        @Override
        public String getDescription() {
            return Translations.OPEN_BLOCKS_SMALL_CHANCE.get();
        }

        @Override
        public double apply(double chance, @Nonnull World world, @Nonnull List<ItemStack> drops, Entity attacker,
            EntityLiving victim) {
            return 0d;
        }
    }
}

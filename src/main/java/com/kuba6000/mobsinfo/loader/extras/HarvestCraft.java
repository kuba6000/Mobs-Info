package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;
import com.pam.harvestcraft.ItemRegistry;

public class HarvestCraft implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntitySheep) {
            MobDrop drop = new MobDrop(
                new ItemStack(ItemRegistry.muttonrawItem),
                MobDrop.DropType.Normal,
                (int) (MobDrop.getChanceBasedOnFromTo(1, 4) * 100d),
                null,
                null,
                true,
                false);
            drop.clampChance();
            drops.add(drop);
        } else if (recipe.entity instanceof EntitySquid) {
            MobDrop drop = new MobDrop(
                new ItemStack(ItemRegistry.calamarirawItem),
                MobDrop.DropType.Normal,
                (int) (MobDrop.getChanceBasedOnFromTo(1, 4) * 100d),
                null,
                null,
                true,
                false);
            drop.clampChance();
            drops.add(drop);
        }
    }
}

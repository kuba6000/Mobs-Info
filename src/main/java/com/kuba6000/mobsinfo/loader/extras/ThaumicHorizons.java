package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.kentington.thaumichorizons.common.entities.EntityMeatSlime;
import com.kentington.thaumichorizons.common.entities.EntityMercurialSlime;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import thaumcraft.common.config.ConfigItems;

public class ThaumicHorizons implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {

        // com.kentington.thaumichorizons.common.lib.EventHandlerEntity.livingDeath

        if (recipe.entity instanceof EntityMeatSlime) {
            MobDrop drop = new MobDrop(
                new ItemStack(Items.beef),
                MobDrop.DropType.Normal,
                2000,
                null,
                null,
                false,
                false);
            drops.add(drop);
            drop = new MobDrop(new ItemStack(Items.porkchop), MobDrop.DropType.Normal, 2000, null, null, false, false);
            drops.add(drop);
            drop = new MobDrop(new ItemStack(Items.chicken), MobDrop.DropType.Normal, 2000, null, null, false, false);
            drops.add(drop);
            drop = new MobDrop(new ItemStack(Items.fish), MobDrop.DropType.Normal, 2000, null, null, false, false);
            drops.add(drop);
            drop = new MobDrop(
                new ItemStack(Items.rotten_flesh),
                MobDrop.DropType.Normal,
                2000,
                null,
                null,
                false,
                false);
            drops.add(drop);
        } else if (recipe.entity instanceof EntityMercurialSlime) {
            MobDrop drop = new MobDrop(
                new ItemStack(ConfigItems.itemResource, 1, 3),
                MobDrop.DropType.Normal,
                10000,
                null,
                null,
                false,
                false);
            drops.add(drop);
        }

    }
}

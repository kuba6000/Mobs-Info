package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import witchinggadgets.common.WGContent;

public class WitchingGadgets implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntityWolf) {
            MobDrop drop = new MobDrop(
                new ItemStack(WGContent.ItemMaterial, 1, 6),
                MobDrop.DropType.Normal,
                6667,
                null,
                null,
                true,
                false);
            drops.add(drop);
        }
    }
}

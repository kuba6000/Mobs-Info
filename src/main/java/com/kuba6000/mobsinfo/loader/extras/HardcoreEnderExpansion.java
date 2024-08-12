package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import chylex.hee.init.ItemList;

public class HardcoreEnderExpansion implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity.getClass() == EntitySilverfish.class) {
            MobDrop drop = new MobDrop(
                new ItemStack(ItemList.silverfish_blood),
                MobDrop.DropType.Normal,
                179,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.chanceModifiers.addAll(
                Arrays.asList(
                    new IChanceModifier.NormalChance(1.79d),
                    new IChanceModifier.OrUsing(Items.golden_sword, 7.14d)));
            drops.add(drop);
        }
    }
}

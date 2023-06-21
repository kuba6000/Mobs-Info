package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import thaumic.tinkerer.common.item.kami.ItemKamiResource;

public class ThaumicTinkerer implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntityEnderman) {
            MobDrop drop = new MobDrop(
                new ItemStack(
                    thaumic.tinkerer.common.ThaumicTinkerer.registry.getFirstItemFromClass(ItemKamiResource.class),
                    1,
                    7),
                MobDrop.DropType.Normal,
                312,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo.addAll(Arrays.asList("Chance: " + 3.12d + "%", "* Only drops in END dimension"));
            drops.add(drop);
        } else if (recipe.entity instanceof EntityPigZombie) {
            MobDrop drop = new MobDrop(
                new ItemStack(
                    thaumic.tinkerer.common.ThaumicTinkerer.registry.getFirstItemFromClass(ItemKamiResource.class),
                    1,
                    6),
                MobDrop.DropType.Normal,
                625,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.variableChanceInfo.addAll(Arrays.asList("Chance: " + 6.25d + "%", "* Only drops in NETHER dimension"));
            drops.add(drop);
        }
    }
}

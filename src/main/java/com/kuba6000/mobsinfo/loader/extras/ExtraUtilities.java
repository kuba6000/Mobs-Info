package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;
import com.rwtema.extrautils.ExtraUtils;

public class ExtraUtilities implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        // com.rwtema.extrautils.EventHandlerServer.soulDropping

        if (ExtraUtils.soul != null && recipe.entity instanceof EntityMob) {
            int prob = 43046721;

            MobDrop drop = new MobDrop(
                new ItemStack(ExtraUtils.soul, 1, 3),
                MobDrop.DropType.Normal,
                0,
                null,
                null,
                false,
                false);
            drop.variableChance = true;
            drop.chanceModifiers.addAll(
                Arrays.asList(
                    new IChanceModifier.NormalChance((1d / (double) prob) * 100d),
                    new IChanceModifier.OrUsing(ExtraUtils.lawSword, (1d / ((double) (prob / 10))) * 100d)));
            drops.add(drop);
        }

        // com.rwtema.extrautils.EventHandlerServer.silverFishDrop

        if (recipe.entity instanceof EntitySilverfish && OreDictionary.getOres("nuggetSilver")
            .size() > 0) {
            ItemStack stack = OreDictionary.getOres("nuggetSilver")
                .get(0)
                .copy();
            if (drops.stream()
                .noneMatch(m -> m.stack.isItemEqual(stack) && m.chance == 10000)) {
                drops.add(
                    new MobDrop(stack, MobDrop.DropType.Normal, (int) ((1d / 5d) * 10000d), null, null, false, false));
            }
        }

        // com.rwtema.extrautils.EventHandlerServer.netherDrop

        if (ExtraUtils.divisionSigil != null && recipe.entity instanceof EntityWither) {
            drops.add(
                new MobDrop(
                    new ItemStack(ExtraUtils.divisionSigil),
                    MobDrop.DropType.Normal,
                    10000,
                    null,
                    null,
                    false,
                    false));
        }

    }

}

package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.items.ItemWispEssence;

public class Thaumcraft implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntityZombie && !(recipe.entity instanceof EntityBrainyZombie)) {
            drops.add(
                new MobDrop(
                    new ItemStack(ConfigItems.itemZombieBrain),
                    MobDrop.DropType.Normal,
                    1000,
                    null,
                    null,
                    false,
                    false));
        }
        if (recipe.entity instanceof EntityVillager) {
            drops.add(
                new MobDrop(
                    new ItemStack(ConfigItems.itemResource, 1, 18),
                    MobDrop.DropType.Normal,
                    1000,
                    null,
                    null,
                    false,
                    false));
        }

        if (recipe.entity.getClass() == EntityWisp.class) {
            ArrayList<Aspect> primals = Aspect.getPrimalAspects();
            int primalcount = primals.size();

            for (Aspect primal : primals) {
                ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
                ((ItemWispEssence) ess.getItem()).setAspects(ess, (new AspectList()).add(primal, 2));
                drops.add(
                    new MobDrop(
                        ess,
                        MobDrop.DropType.Normal,
                        (int) ((0.9d * (1d / (double) primalcount)) * 10000d),
                        null,
                        null,
                        false,
                        false));
            }
            ArrayList<Aspect> compounds = Aspect.getCompoundAspects();
            int compoundcount = compounds.size();
            for (Aspect compound : compounds) {
                ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
                ((ItemWispEssence) ess.getItem()).setAspects(ess, (new AspectList()).add(compound, 2));
                drops.add(
                    new MobDrop(
                        ess,
                        MobDrop.DropType.Normal,
                        (int) ((0.1d * (1d / (double) compoundcount)) * 10000d),
                        null,
                        null,
                        false,
                        false));
            }
        }
    }
}

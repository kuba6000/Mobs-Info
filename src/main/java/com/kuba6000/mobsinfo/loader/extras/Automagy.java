package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;
import tuhljin.automagy.entities.EntityWispNether;

public class Automagy implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity.getClass() == EntityWispNether.class) {
            Aspect infernus = Aspect.getAspect("infernus");
            if (infernus == null) {
                ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
                ((ItemWispEssence) ess.getItem()).setAspects(ess, (new AspectList()).add(Aspect.FIRE, 2));
                drops.add(new MobDrop(ess, MobDrop.DropType.Normal, 10000, null, null, false, false));
            } else {
                ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
                ((ItemWispEssence) ess.getItem()).setAspects(ess, (new AspectList()).add(Aspect.FIRE, 2));
                drops.add(new MobDrop(ess, MobDrop.DropType.Normal, 7500, null, null, false, false));
                ess = new ItemStack(ConfigItems.itemWispEssence);
                ((ItemWispEssence) ess.getItem()).setAspects(ess, (new AspectList()).add(infernus, 2));
                drops.add(new MobDrop(ess, MobDrop.DropType.Normal, 2500, null, null, false, false));
            }
        }
    }
}

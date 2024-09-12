package com.kuba6000.mobsinfo.loader.extras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.IChanceModifier;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import xreliquary.event.CommonEventHandler;
import xreliquary.init.XRRecipes;
import xreliquary.lib.Names;
import xreliquary.lib.Reference;

public class Reliquarry implements IExtraLoader {

    private CommonEventHandler eventHandler;

    public Reliquarry() {
        this.eventHandler = new CommonEventHandler();
    }

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        if (recipe.entity instanceof EntitySquid) {
            addDrop(drops, XRRecipes.ingredient(Reference.SQUID_INGREDIENT_META), Names.squid_beak);
        } else if (recipe.entity instanceof EntityWitch) {
            Item witchHatItem = XRRecipes.getItem(Names.witch_hat);
            ItemStack witchHatStack = new ItemStack(witchHatItem, 1);
            addDrop(drops, witchHatStack, Names.witch_hat);
        } else if (recipe.entity instanceof EntitySpider) {
            addDrop(drops, XRRecipes.ingredient(Reference.SPIDER_INGREDIENT_META), Names.spider_fangs);
        } else if (recipe.entity instanceof EntityCaveSpider) {
            addDrop(drops, XRRecipes.ingredient(Reference.SPIDER_INGREDIENT_META), Names.cave_spider_fangs);
        } else if (recipe.entity instanceof EntitySkeleton) {
            addDrop(drops, XRRecipes.ingredient(Reference.SKELETON_INGREDIENT_META), Names.rib_bone);
            if (((EntitySkeleton) recipe.entity).getSkeletonType() == 1) {
                addDrop(drops, XRRecipes.ingredient(Reference.WITHER_INGREDIENT_META), Names.withered_rib);
            }
        } else if (recipe.entity instanceof EntityZombie) {
            addDrop(drops, XRRecipes.ingredient(Reference.ZOMBIE_INGREDIENT_META), Names.zombie_heart);
        } else if (recipe.entity instanceof EntityPigZombie) {
            addDrop(drops, XRRecipes.ingredient(Reference.ZOMBIE_INGREDIENT_META), Names.pigman_heart);
        } else if (recipe.entity instanceof EntitySlime) {
            addDrop(drops, XRRecipes.ingredient(Reference.SLIME_INGREDIENT_META), Names.slime_pearl);
        } else if (recipe.entity instanceof EntityBlaze) {
            addDrop(drops, XRRecipes.ingredient(Reference.MOLTEN_INGREDIENT_META), Names.blaze_molten_core);
        } else if (recipe.entity instanceof EntityMagmaCube) {
            addDrop(drops, XRRecipes.ingredient(Reference.MOLTEN_INGREDIENT_META), Names.magma_cube_molten_core);
        } else if (recipe.entity instanceof EntityGhast) {
            addDrop(drops, XRRecipes.ingredient(Reference.CREEPER_INGREDIENT_META), Names.ghast_gland);
        } else if (recipe.entity instanceof EntityCreeper) {
            addDrop(drops, XRRecipes.ingredient(Reference.CREEPER_INGREDIENT_META), Names.creeper_gland);
            if (((EntityCreeper) recipe.entity).getPowered()) {
                addDrop(drops, XRRecipes.ingredient(Reference.STORM_INGREDIENT_META), Names.eye_of_the_storm);
            }
        } else if (recipe.entity instanceof EntityEnderman) {
            addDrop(drops, XRRecipes.ingredient(Reference.ENDER_INGREDIENT_META), Names.ender_heart);
        } else if (recipe.entity instanceof EntityBat) {
            addDrop(drops, XRRecipes.ingredient(Reference.BAT_INGREDIENT_META), Names.bat_wing);
        } else if (recipe.entity instanceof EntitySnowman) {
            addDrop(drops, XRRecipes.ingredient(Reference.FROZEN_INGREDIENT_META), Names.frozen_core);
        }
    }

    private void addDrop(ArrayList<MobDrop> drops, ItemStack item, String name) {
        MobDrop drop = new MobDrop(item, MobDrop.DropType.Normal, (int)(eventHandler.getBaseDrop(name) * 10000d), null, null, false, false);
        drops.add(drop);
    }

    private static class ReliquaryDropChance implements IChanceModifier, Serializable {

        private static final long serialVersionUID = 1L;
        private String dropName;
        private transient double baseChance;
        private transient CommonEventHandler eventHandler;

        public ReliquaryDropChance() {}

        ReliquaryDropChance(String dropName, CommonEventHandler eventHandler) {
            this.dropName = dropName;
            this.eventHandler = eventHandler;
            this.baseChance = eventHandler.getBaseDrop(dropName);
        }

        @Override
        public String getDescription() {
            double percentage = baseChance * 100;
            return String.format("Percentage: %.2f%%", percentage);
        }

        @Override
        public double apply(double chance, World world, List<ItemStack> drops, Entity attacker, EntityLiving victim) {
            // Recalculate baseChance if it's 0 (e.g., after deserialization)
            if (baseChance == 0 && eventHandler != null) {
                baseChance = eventHandler.getBaseDrop(dropName);
            }
            return baseChance;
        }
    }
}

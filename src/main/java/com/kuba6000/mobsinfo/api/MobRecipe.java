package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.helper.EnderIOHelper;
import com.kuba6000.mobsinfo.config.Config;
import com.kuba6000.mobsinfo.loader.MobRecipeLoader;
import com.kuba6000.mobsinfo.mixin.InfernalMobs.InfernalMobsCoreAccessor;

import atomicstryker.infernalmobs.common.InfernalMobsCore;

public class MobRecipe {

    public final ArrayList<MobDrop> mOutputs;
    public int mMaxDamageChance;
    public final boolean infernalityAllowed;
    public final boolean alwaysinfernal;
    public static MobRecipeLoader.droplist infernaldrops;
    public final boolean isPeacefulAllowed;
    public final EntityLiving entity;
    public final float maxEntityHealth;
    public final boolean isUsableInVial;

    @SuppressWarnings("unchecked")
    public MobRecipe copy() {
        return new MobRecipe(
            (ArrayList<MobDrop>) mOutputs.clone(),
            mMaxDamageChance,
            infernalityAllowed,
            alwaysinfernal,
            isPeacefulAllowed,
            entity,
            maxEntityHealth,
            isUsableInVial);
    }

    private MobRecipe(ArrayList<MobDrop> mOutputs, int mMaxDamageChance, boolean infernalityAllowed,
        boolean alwaysinfernal, boolean isPeacefulAllowed, EntityLiving entity, float maxEntityHealth,
        boolean isUsable) {
        this.mOutputs = mOutputs;
        this.mMaxDamageChance = mMaxDamageChance;
        this.infernalityAllowed = infernalityAllowed;
        this.alwaysinfernal = alwaysinfernal;
        this.isPeacefulAllowed = isPeacefulAllowed;
        this.entity = entity;
        this.maxEntityHealth = maxEntityHealth;
        this.isUsableInVial = isUsable;
    }

    public static MobRecipe generateMobRecipe(EntityLiving e, String entityID, ArrayList<MobDrop> outputs) {
        return new MobRecipe(e, entityID, outputs);
    }

    @SuppressWarnings("unchecked")
    private MobRecipe(EntityLiving e, String entityID, ArrayList<MobDrop> outputs) {
        if (LoaderReference.InfernalMobs) {
            InfernalMobsCoreAccessor infernalMobsCore = (InfernalMobsCoreAccessor) InfernalMobsCore.instance();
            if (infernaldrops == null) {
                infernaldrops = MobRecipeLoader.getInfernalDrops();
            }
            infernalityAllowed = infernalMobsCore.callIsClassAllowed(e);
            alwaysinfernal = infernalMobsCore.callCheckEntityClassForced(e);
        } else {
            infernalityAllowed = false;
            alwaysinfernal = false;
        }

        if (infernaldrops == null) infernaldrops = new MobRecipeLoader.droplist();

        isPeacefulAllowed = !(e instanceof IMob);

        mOutputs = (ArrayList<MobDrop>) outputs.clone();
        int maxdamagechance = 0;
        for (MobDrop o : mOutputs) if (o.damages != null) for (int v : o.damages.values()) maxdamagechance += v;
        mMaxDamageChance = maxdamagechance;
        maxEntityHealth = e.getMaxHealth();
        entity = e;
        isUsableInVial = EnderIOHelper.canEntityBeCapturedWithSoulVial(e, entityID);
    }

    public void refresh() {
        int maxdamagechance = 0;
        for (MobDrop o : mOutputs) if (o.damages != null) for (int v : o.damages.values()) maxdamagechance += v;
        mMaxDamageChance = maxdamagechance;
    }

    public ItemStack[] generateRandomOutputs(World world, Random rnd, int lootinglevel,
        boolean includeInfernalDropsIfPossible) {
        ArrayList<ItemStack> stacks = new ArrayList<>(mOutputs.size());
        for (MobDrop o : mOutputs) {
            int chance = o.chance;
            if (o.playerOnly) {
                chance = (int) ((double) chance * Config.MobHandler.playerOnlyDropsModifier);
                if (chance < 1) chance = 1;
            }
            int amount = o.stack.stackSize;
            if (o.lootable && lootinglevel > 0) {
                chance += lootinglevel * 5000;
                if (chance > 10000) {
                    int div = (int) Math.ceil(chance / 10000d);
                    amount *= div;
                    chance /= div;
                }
            }
            if (chance == 10000 || rnd.nextInt(10000) < chance) {
                ItemStack s = o.stack.copy();
                s.stackSize = amount;
                if (o.enchantable != null) EnchantmentHelper.addRandomEnchantment(rnd, s, o.enchantable);
                if (o.damages != null) {
                    int rChance = rnd.nextInt(mMaxDamageChance);
                    int cChance = 0;
                    for (Map.Entry<Integer, Integer> damage : o.damages.entrySet()) {
                        cChance += damage.getValue();
                        if (rChance <= cChance) {
                            s.setItemDamage(damage.getKey());
                            break;
                        }
                    }
                }
                stacks.add(s);
            }
        }

        if (LoaderReference.InfernalMobs && includeInfernalDropsIfPossible) {
            InfernalMobsCoreAccessor infernalMobsCore = (InfernalMobsCoreAccessor) InfernalMobsCore.instance();
            if (infernalityAllowed && !infernalMobsCore.getDimensionBlackList()
                .contains(world.provider.dimensionId)) {
                int p = 0;
                int mods = 0;
                if (alwaysinfernal || (rnd.nextInt(infernalMobsCore.getEliteRarity()) == 0)) {
                    p = 1;
                    if (rnd.nextInt(infernalMobsCore.getUltraRarity()) == 0) {
                        p = 2;
                        if (rnd.nextInt(infernalMobsCore.getInfernoRarity()) == 0) p = 3;
                    }
                }
                ArrayList<ItemStack> infernalstacks = null;
                if (p > 0) if (p == 1) {
                    infernalstacks = infernalMobsCore.getDropIdListElite();
                    mods = infernalMobsCore.getMinEliteModifiers();
                } else if (p == 2) {
                    infernalstacks = infernalMobsCore.getDropIdListUltra();
                    mods = infernalMobsCore.getMinUltraModifiers();
                } else {
                    infernalstacks = infernalMobsCore.getDropIdListInfernal();
                    mods = infernalMobsCore.getMinInfernoModifiers();
                }
                if (infernalstacks != null) {
                    ItemStack infernalstack = infernalstacks.get(rnd.nextInt(infernalstacks.size()))
                        .copy();
                    // noinspection ConstantConditions
                    EnchantmentHelper.addRandomEnchantment(
                        rnd,
                        infernalstack,
                        infernalstack.getItem()
                            .getItemEnchantability());
                    stacks.add(infernalstack);
                }
            }
        }

        return stacks.toArray(new ItemStack[0]);
    }

    public static HashMap<String, MobRecipe> MobNameToRecipeMap = new HashMap<>();

    public static MobRecipe getRecipeByEntityName(String mobName) {
        return MobNameToRecipeMap.get(mobName);
    }
}

package com.kuba6000.mobsinfo.api;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLiving;

import cpw.mods.fml.common.registry.VillagerRegistry;

/**
 * Mobs Info manager, you can register direct generation overrides here.
 * If you are looking for providing your own mob drops / event drops / villager trades, look at
 * {@link IMobInfoProvider}, {@link IMobExtraInfoProvider},
 * {@link IVillagerInfoProvider} instead
 */
public abstract class IMobsInfoManager {

    /**
     * Instance, this variable is initialized in pre-init phase, use only after that phase (init for example)!
     */
    public static IMobsInfoManager instance = null;

    /**
     * Override mob info at generation time, it will completely prevent default generation methods and use yours
     * instead! DO NOT USE THIS ON YOUR MOBS!
     *
     * @param mob      Mob to override
     * @param provider Custom provider
     */
    public abstract void overrideMobInfoGenerationHandler(@Nonnull Class<? extends EntityLiving> mob,
        @Nonnull IMobInfoProvider provider);

    /**
     * Override event extra information. DO NOT USE THIS ON YOUR EVENTS!
     *
     * @param event    Event handler to override
     * @param provider Provider
     */
    public abstract void overrideMobExtraInfoHandler(@Nonnull Class<?> event, @Nonnull IMobExtraInfoProvider provider);

    /**
     * Override villager info at generation time, it will completely prevent default generation methods and use yours
     * instead! DO NOT USE THIS ON YOUR VILLAGERS!
     *
     * @param tradeHandler Trade handler to override
     * @param provider     Provider
     */
    public abstract void overrideVillagerInfoGenerationHandler(
        @Nonnull Class<? extends VillagerRegistry.IVillageTradeHandler> tradeHandler,
        @Nonnull IVillagerInfoProvider provider);

}

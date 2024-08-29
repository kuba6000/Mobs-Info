package com.kuba6000.mobsinfo.loader;

import static com.kuba6000.mobsinfo.MobsInfo.MODID;

import java.util.HashMap;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLiving;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kuba6000.mobsinfo.api.IMobExtraInfoProvider;
import com.kuba6000.mobsinfo.api.IMobInfoProvider;
import com.kuba6000.mobsinfo.api.IMobsInfoManager;
import com.kuba6000.mobsinfo.api.IVillagerInfoProvider;

import cpw.mods.fml.common.registry.VillagerRegistry;

public class MobsInfoManager extends IMobsInfoManager {

    private static final Logger LOG = LogManager.getLogger(MODID + "[Manager]");
    static final HashMap<Class<? extends EntityLiving>, IMobInfoProvider> customMobProviders = new HashMap<>();
    static final HashMap<Class<?>, IMobExtraInfoProvider> customMobExtraInfoProviders = new HashMap<>();
    static final HashMap<Class<? extends VillagerRegistry.IVillageTradeHandler>, IVillagerInfoProvider> customVillagerProviders = new HashMap<>();

    @Override
    public void overrideMobInfoGenerationHandler(@Nonnull Class<? extends EntityLiving> mob,
        @Nonnull IMobInfoProvider provider) {
        if (provider instanceof EntityLiving)
            throw new RuntimeException("Normal IMobInfoProvider used to make override!");
        if (IMobInfoProvider.class.isAssignableFrom(mob)) {
            LOG.warn(
                "WARNING! Class {} was used to override {} while it already overrides IMobInfoProvider!",
                provider.getClass()
                    .getName(),
                mob.getName());
        }
        IMobInfoProvider previous = customMobProviders.putIfAbsent(mob, provider);
        if (previous != null) {
            LOG.warn(
                "WARNING! Class {} was already overridden by {}, ignoring new override ({})!",
                mob.getName(),
                previous.getClass()
                    .getName(),
                provider.getClass()
                    .getName());
        }
    }

    @Override
    public void overrideMobExtraInfoHandler(@Nonnull Class<?> event, @Nonnull IMobExtraInfoProvider provider) {
        if (IMobExtraInfoProvider.class.isAssignableFrom(event)) {
            LOG.warn(
                "WARNING! Class {} was used to override {} while it already overrides IMobExtraInfoProvider!",
                provider.getClass()
                    .getName(),
                event.getName());
        }
        IMobExtraInfoProvider previous = customMobExtraInfoProviders.putIfAbsent(event, provider);
        if (previous != null) {
            LOG.warn(
                "WARNING! Class {} was already overridden by {}, ignoring new override ({})!",
                event.getName(),
                previous.getClass()
                    .getName(),
                provider.getClass()
                    .getName());
        }
    }

    @Override
    public void overrideVillagerInfoGenerationHandler(
        @Nonnull Class<? extends VillagerRegistry.IVillageTradeHandler> tradeHandler,
        @Nonnull IVillagerInfoProvider provider) {
        if (provider instanceof VillagerRegistry.IVillageTradeHandler)
            throw new RuntimeException("Normal IVillagerInfoProvider used to make override!");
        if (IVillagerInfoProvider.class.isAssignableFrom(tradeHandler)) {
            LOG.warn(
                "WARNING! Class {} was used to override {} while it already overrides IVillagerInfoProvider!",
                provider.getClass()
                    .getName(),
                tradeHandler.getName());
        }
        IVillagerInfoProvider previous = customVillagerProviders.putIfAbsent(tradeHandler, provider);
        if (previous != null) {
            LOG.warn(
                "WARNING! Class {} was already overridden by {}, ignoring new override ({})!",
                tradeHandler.getName(),
                previous.getClass()
                    .getName(),
                provider.getClass()
                    .getName());
        }
    }

}

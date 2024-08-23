package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.entity.passive.EntityVillager;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

/**
 * You should add this interface on your villager handler (on a class that implements {@link IVillageTradeHandler})
 */
public interface IVillagerInfoProvider {

    /**
     * You should provide information about all the things that your handler can add to trade list:
     *
     * @param villager   Villager
     * @param profession Profession id
     * @param trades     Trade list
     */
    void provideTrades(@Nonnull final EntityVillager villager, final int profession,
        @Nonnull ArrayList<VillagerTrade> trades);

}

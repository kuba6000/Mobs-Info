package com.kuba6000.mobsinfo.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.registry.VillagerRegistry;

@Mixin(value = VillagerRegistry.class)
public interface VillagerRegistryAccessor {

    @Accessor
    Multimap<Integer, VillagerRegistry.IVillageTradeHandler> getTradeHandlers();

}

package com.kuba6000.mobsinfo.api.event;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import com.kuba6000.mobsinfo.api.MobOverride;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * PostMobsOverridesLoadEvent event. Gets fired on {@link MinecraftForge#EVENT_BUS}.
 * Gets fired when the override config is loaded.
 * You can make modifications here.
 * Gets fired only Server sided.
 * This event is not cancellable.
 *
 */
public class PostMobsOverridesLoadEvent extends Event {

    /**
     * Overrides map. (Mob registry name) -> {@link MobOverride}
     *
     */
    public final Map<String, MobOverride> overrides;

    public PostMobsOverridesLoadEvent(Map<String, MobOverride> overrides) {
        this.overrides = overrides;
    }
}

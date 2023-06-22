package com.kuba6000.mobsinfo.api.event;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * PreMobsRegistrationEvent event. Gets fired on {@link MinecraftForge#EVENT_BUS}.
 * Gets fired before mobs registration has begun.
 * Gets fired both on Server and Client.
 * This event is not cancellable.
 *
 */
public class PreMobsRegistrationEvent extends Event {

    public PreMobsRegistrationEvent() {}
}

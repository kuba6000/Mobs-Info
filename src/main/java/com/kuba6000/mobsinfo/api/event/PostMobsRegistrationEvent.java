package com.kuba6000.mobsinfo.api.event;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * PostMobsRegistrationEvent event. Gets fired on {@link MinecraftForge#EVENT_BUS}.
 * Gets fired when mobs registration is finished.
 * Gets fired both on Server and Client.
 * This event is not cancellable.
 *
 */
public class PostMobsRegistrationEvent extends Event {

    public PostMobsRegistrationEvent() {}
}

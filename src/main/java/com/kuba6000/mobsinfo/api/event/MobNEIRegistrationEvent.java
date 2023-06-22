package com.kuba6000.mobsinfo.api.event;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * MobNEIRegistrationEvent event. Gets fired on {@link MinecraftForge#EVENT_BUS}.
 * Gets fired when mob is being registered in NEI
 * You can add information to the NEI page here.
 * Gets fired only Client sided.
 * This event is not cancellable.
 *
 */
public class MobNEIRegistrationEvent extends Event {

    /**
     * Mob registry name
     *
     */
    public final String mobName;

    /**
     * Mob instance. DO NOT MODIFY
     *
     */
    public final EntityLiving mob;

    /**
     * Additional NEI information
     *
     */
    public final List<String> additionalInformation;

    public MobNEIRegistrationEvent(String mobName, EntityLiving mob, List<String> additionalInformation) {
        this.mobName = mobName;
        this.mob = mob;
        this.additionalInformation = additionalInformation;
    }
}

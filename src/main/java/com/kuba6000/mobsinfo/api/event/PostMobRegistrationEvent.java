package com.kuba6000.mobsinfo.api.event;

import java.util.ArrayList;

import net.minecraftforge.common.MinecraftForge;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * PostMobRegistrationEvent event. Gets fired on {@link MinecraftForge#EVENT_BUS}.
 * Gets fired when mob is fully loaded (including overrides config) and before NEI registration.
 * You can make modifications here.
 * Gets fired both on Server and Client.
 * This event is not cancellable.
 *
 */
public class PostMobRegistrationEvent extends Event {

    /**
     * Mob registry name
     *
     */
    public final String currentMob;
    /**
     * Mob drops, you can edit them.
     *
     */
    public final ArrayList<MobDrop> drops;
    /**
     * Mob recipe
     *
     */
    public final MobRecipe recipe;

    public PostMobRegistrationEvent(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        this.currentMob = k;
        this.drops = drops;
        this.recipe = recipe;
    }
}

package com.kuba6000.mobsinfo.api.event;

import java.util.ArrayList;

import net.minecraftforge.common.MinecraftForge;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * PreMobRegistrationEvent event. Gets fired on {@link MinecraftForge#EVENT_BUS}.
 * Gets fired before any modifications to the mob are done.
 * You can make modifications here.
 * Gets fired both on Server and Client.
 * When the event is cancelled, the mob will not be added to NEI.
 *
 */
@Cancelable
public class PreMobRegistrationEvent extends Event {

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

    public PreMobRegistrationEvent(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
        this.currentMob = k;
        this.drops = drops;
        this.recipe = recipe;
    }
}

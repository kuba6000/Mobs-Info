package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

/**
 * You should add this interface on a class that contains {@link LivingDropsEvent} or {@link LivingDeathEvent} that add
 * drops to ANY mob
 */
public interface IMobExtraInfoProvider {

    void provideExtraDropsInformation(String k, ArrayList<MobDrop> drops, MobRecipe recipe);

}

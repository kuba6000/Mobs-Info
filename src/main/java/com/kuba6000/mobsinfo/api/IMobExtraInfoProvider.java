package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

/**
 * You should add this interface on a class that contains {@link LivingDropsEvent} or {@link LivingDeathEvent} that add
 * drops to ANY mob
 */
public interface IMobExtraInfoProvider {

    /**
     * This method runs for every registered entity just like {@link LivingDropsEvent} or {@link LivingDeathEvent}.
     * You should provide all changes that you make in events. You should modify provided drops list, DO NOT EDIT
     * {@link MobRecipe#mOutputs}, IT WON'T WORK!
     * You can check for actual entity using {@link MobRecipe#entity} instead of entityString
     * 
     * @param entityString Entity registration name
     * @param drops        Drop list (you should edit that)
     * @param recipe       Recipe
     */
    void provideExtraDropsInformation(@Nonnull final String entityString, @Nonnull final ArrayList<MobDrop> drops,
        @Nonnull final MobRecipe recipe);

}

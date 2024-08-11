package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * You should add this interface on your mobs (on a class that extends {@link EntityLiving})
 * If you are looking for a provider for special drops (like tinkers beheading), look at {@link IMobExtraInfoProvider}
 */
public interface IMobInfoProvider {

    /**
     * You should provide information about all the things that your mob can drop in:
     * {@link EntityLivingBase#dropFewItems(boolean, int)},
     * {@link EntityLivingBase#dropRareDrop(int)},
     * {@link EntityLiving#addRandomArmor()} + {@link EntityLiving#enchantEquipment()}
     *
     * @param drops
     */
    void provideDropsInformation(@Nonnull ArrayList<MobDrop> drops);

}

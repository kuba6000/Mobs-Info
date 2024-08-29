package com.kuba6000.mobsinfo.api;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import com.kuba6000.mobsinfo.loader.MobRecipeLoader;
import com.kuba6000.mobsinfo.loader.VanillaMobRecipeLoader;

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
     * @param drops Add drops to this list (use {@link MobDrop#create} to create drops)
     */
    void provideDropsInformation(@Nonnull ArrayList<MobDrop> drops);

    /**
     * Add drops from vanilla mob
     *
     * @param drops        Drops from provideDropsInformation
     * @param entityString Entity String
     * @param type         Desired drop type or null to get add of them
     */
    static void provideSuperVanillaDrops(@Nonnull ArrayList<MobDrop> drops, @Nonnull String entityString,
        @Nullable MobDrop.DropType type) {
        MobRecipeLoader.GeneralMappedMob mob = VanillaMobRecipeLoader.vanillaMobList.get(entityString);
        if (mob != null) {
            for (MobDrop drop : mob.drops) {
                if (type == null || type == drop.type) drops.add(drop.copy());
            }
        }
    }

    /**
     * Add drops from vanilla mob
     *
     * @param drops      Drops from provideDropsInformation
     * @param vanillaMob Entity class
     * @param type       Desired drop type or null to get add of them
     */
    static void provideSuperVanillaDrops(@Nonnull ArrayList<MobDrop> drops,
        @Nonnull Class<? extends EntityLiving> vanillaMob, @Nullable MobDrop.DropType type) {
        provideSuperVanillaDrops(drops, EntityList.classToStringMapping.get(vanillaMob), type);
    }

    /**
     * Add drops from vanilla mob
     *
     * @param drops      Drops from provideDropsInformation
     * @param vanillaMob Entity class
     */
    static void provideSuperVanillaDrops(@Nonnull ArrayList<MobDrop> drops,
        @Nonnull Class<? extends EntityLiving> vanillaMob) {
        provideSuperVanillaDrops(drops, EntityList.classToStringMapping.get(vanillaMob), null);
    }

    /**
     * Add drops from vanilla mob
     *
     * @param drops        Drops from provideDropsInformation
     * @param entityString Entity String
     */
    static void provideSuperVanillaDrops(@Nonnull ArrayList<MobDrop> drops, @Nonnull String entityString) {
        provideSuperVanillaDrops(drops, entityString, null);
    }

}

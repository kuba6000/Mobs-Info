package com.kuba6000.mobsinfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.kuba6000.mobsinfo.network.SaveDataPacket;
import com.kuba6000.mobsinfo.savedata.PlayerData;
import com.kuba6000.mobsinfo.savedata.PlayerDataManager;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.isCanceled()) return;

        DamageSource source = event.source;
        if (source == null) return;

        Entity sourceEntity = source.getEntity();

        if (!(sourceEntity instanceof EntityPlayerMP)) return;

        PlayerData playerData = PlayerDataManager.getPlayer((EntityPlayer) sourceEntity);

        String entity = EntityList.getEntityString(event.entityLiving);

        if (!playerData.killedMobs.contains(entity)) {
            playerData.killedMobs.add(entity);
            MobsInfo.NETWORK.sendTo(new SaveDataPacket(playerData), (EntityPlayerMP) sourceEntity);
        }
    }

}

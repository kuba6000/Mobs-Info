package com.kuba6000.mobsinfo;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.kuba6000.mobsinfo.api.IMobExtraInfoProvider;
import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;
import com.kuba6000.mobsinfo.config.Config;
import com.kuba6000.mobsinfo.network.SaveDataPacket;
import com.kuba6000.mobsinfo.savedata.PlayerData;
import com.kuba6000.mobsinfo.savedata.PlayerDataManager;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler implements IMobExtraInfoProvider {

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.isCanceled()) return;

        DamageSource source = event.source;
        if (source == null) return;

        Entity sourceEntity = source.getEntity();

        if (!(sourceEntity instanceof EntityPlayerMP) || sourceEntity instanceof FakePlayer
            || event.entityLiving instanceof EntityPlayer) return;

        PlayerData playerData = PlayerDataManager.getPlayer((EntityPlayer) sourceEntity);

        String entity = EntityList.getEntityString(event.entityLiving);

        if (entity != null && !entity.isEmpty()
            && MobRecipe.getRecipeByEntityName(entity) != null
            && !playerData.killedMobs.contains(entity)) {
            playerData.killedMobs.add(entity);
            playerData.markDirty();
            MobsInfo.NETWORK.sendTo(new SaveDataPacket(playerData), (EntityPlayerMP) sourceEntity);
            if (Config.MobHandler.hiddenMode && MobRecipe.getRecipeByEntityName(entity) != null) {
                ((EntityPlayerMP) sourceEntity).addChatMessage(
                    new ChatComponentText(StatCollector.translateToLocal("mobsinfo.mobhandler.unlocked")));
            }
        }
    }

    @Override
    public void provideExtraDropsInformation(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {

    }
}

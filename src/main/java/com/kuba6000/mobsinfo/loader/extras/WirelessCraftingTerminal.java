package com.kuba6000.mobsinfo.loader.extras;

import java.util.ArrayList;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.ItemStack;
import net.p455w0rd.wirelesscraftingterminal.items.ItemEnum;
import net.p455w0rd.wirelesscraftingterminal.reference.Reference;

import com.kuba6000.mobsinfo.api.MobDrop;
import com.kuba6000.mobsinfo.api.MobRecipe;

public class WirelessCraftingTerminal implements IExtraLoader {

    @Override
    public void process(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {

        // net.p455w0rd.wirelesscraftingterminal.proxy.CommonProxy.onMobDrop

        if (!Reference.WCT_EASYMODE_ENABLED && Reference.WCT_BOOSTER_ENABLED && Reference.WCT_BOOSTERDROP_ENABLED) {
            if (recipe.entity instanceof EntityDragon) {
                drops.add(
                    new MobDrop(
                        new ItemStack(ItemEnum.BOOSTER_CARD.getItem()),
                        MobDrop.DropType.Normal,
                        10000,
                        null,
                        null,
                        false,
                        false));
            } else if (recipe.entity instanceof EntityWither) {
                drops.add(
                    new MobDrop(
                        new ItemStack(ItemEnum.BOOSTER_CARD.getItem()),
                        MobDrop.DropType.Normal,
                        5000,
                        null,
                        null,
                        false,
                        false));
            }
        }
    }
}

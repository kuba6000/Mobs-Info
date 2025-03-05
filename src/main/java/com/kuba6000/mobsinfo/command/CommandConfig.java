/*
 * spotless:off
 * MobsInfo - Minecraft addon
 * Copyright (C) 2023-2025  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package com.kuba6000.mobsinfo.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import com.kuba6000.mobsinfo.MobsInfo;
import com.kuba6000.mobsinfo.config.Config;
import com.kuba6000.mobsinfo.loader.MobRecipeLoader;
import com.kuba6000.mobsinfo.network.LoadConfigPacket;

public class CommandConfig extends CommandBase {

    enum Translations {

        INVALID_OPTION,
        SUCCESS,
        USAGE,;

        final String key;

        Translations() {
            key = "mobsinfo.command.config." + this.name()
                .toLowerCase();
        }

        public String get() {
            return StatCollector.translateToLocal(key);
        }

        public String get(Object... args) {
            return StatCollector.translateToLocalFormatted(key, args);
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return get();
        }
    }

    @Override
    public String getCommandName() {
        return "config";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "config " + Translations.USAGE.get();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length == 0 || !p_71515_2_[0].equals("reload")) {
            p_71515_1_.addChatMessage(new ChatComponentText(Translations.INVALID_OPTION.get()));
            return;
        }
        Config.synchronizeConfiguration();
        MobRecipeLoader.processMobRecipeMap();
        MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList.forEach(p -> {
                if (!(p instanceof EntityPlayerMP)) return;
                MobsInfo.info("Sending config to " + ((EntityPlayerMP) p).getDisplayName());
                MobsInfo.NETWORK.sendTo(LoadConfigPacket.instance, (EntityPlayerMP) p);
            });
        p_71515_1_.addChatMessage(new ChatComponentText(Translations.SUCCESS.get()));
    }
}

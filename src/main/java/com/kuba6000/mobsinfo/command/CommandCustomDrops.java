/*
 * spotless:off
 * MobsInfo - Minecraft addon
 * Copyright (C) 2023-2024  kuba6000
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

import java.io.File;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.kuba6000.mobsinfo.api.utils.ModUtils;
import com.kuba6000.mobsinfo.loader.MobRecipeLoader;

public class CommandCustomDrops extends CommandBase {

    @Override
    public String getCommandName() {
        return "customdrops";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "customdrops";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {

        if (!ModUtils.isClientSided) {
            p_71515_1_
                .addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This command is single-player only!"));
            return;
        }

        File f = MobRecipeLoader.makeCustomDrops();

        if (f == null) {
            p_71515_1_.addChatMessage(
                new ChatComponentText(EnumChatFormatting.RED + "There was an error! Look in the console"));
        } else p_71515_1_.addChatMessage(new ChatComponentText(f.getAbsolutePath()));
    }
}

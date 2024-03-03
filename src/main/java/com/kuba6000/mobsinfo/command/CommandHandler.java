/*
 * spotless:off
 * MobsInfo - Minecraft addon
 * Copyright (C) 2023  kuba6000
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class CommandHandler extends CommandBase {

    enum Translations {

        INVALID,
        CANT_FIND,
        GENERIC_HELP,
        USAGE,;

        final String key;

        Translations() {
            key = "mobsinfo.commandhandler." + this.name()
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

    private static final List<String> aliases = Collections.singletonList("mi");
    public static final HashMap<String, ICommand> commands = new HashMap<>();

    @Override
    public String getCommandName() {
        return "mobsinfo";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "mobsinfo " + Translations.USAGE.get();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_1_.getEntityWorld().isRemote) return;
        if (p_71515_2_.length == 0) {
            p_71515_1_.addChatMessage(new ChatComponentText(Translations.INVALID.get(getCommandUsage(p_71515_1_))));
            p_71515_1_.addChatMessage(new ChatComponentText(Translations.GENERIC_HELP.get()));
            return;
        }
        if (!commands.containsKey(p_71515_2_[0])) {
            p_71515_1_.addChatMessage(new ChatComponentText(Translations.CANT_FIND.get(p_71515_2_[0])));
            p_71515_1_.addChatMessage(new ChatComponentText(Translations.GENERIC_HELP.get()));
            return;
        }
        ICommand cmd = commands.get(p_71515_2_[0]);
        if (!cmd.canCommandSenderUseCommand(p_71515_1_)) {
            ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation(
                "commands.generic.permission");
            chatcomponenttranslation2.getChatStyle()
                .setColor(EnumChatFormatting.RED);
            p_71515_1_.addChatMessage(chatcomponenttranslation2);
        } else cmd.processCommand(
            p_71515_1_,
            p_71515_2_.length > 1 ? Arrays.copyOfRange(p_71515_2_, 1, p_71515_2_.length) : new String[0]);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    public static void addCommand(ICommand command) {
        commands.put(command.getCommandName(), command);
    }

    static {
        addCommand(new CommandHelp());
        addCommand(new CommandConfig());
        addCommand(new CommandCustomDrops());
        addCommand(new CommandHidden());
        addCommand(new CommandHandlers());
    }

}

package com.kuba6000.mobsinfo.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.kuba6000.mobsinfo.api.utils.ModUtils;
import com.kuba6000.mobsinfo.loader.EventDiscovery;

public class CommandHandlers extends CommandBase {

    @Override
    public String getCommandName() {
        return "handlers";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "handlers";
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

        EventDiscovery.run();
        p_71515_1_.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Look in the console"));
    }

}

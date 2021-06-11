package dev.pace.reboot.commands;

import dev.pace.reboot.Reboot;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Pace
 * No part of this publication may be reproduced, disturbed, or transmitted in any form or any means.
 */

public class RebootReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("reboot.reload") || !p.isOp()) {
            p.sendMessage(ChatColor.RED + "You are not allowed to execute this command. Contact a server administrator if you believe this is an error.");
            return true;
        }
        try {
            Reboot.getInstance().reloadConfiguration();
            sender.sendMessage(ChatColor.GREEN + "Config has been reloaded!");
        } catch (NullPointerException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Error! Check console for more details.");
        }
        return true;
    }
}
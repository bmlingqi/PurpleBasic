package com.bemil.purplebasic.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messager {
    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                message
        ));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                message
        ));
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(
                '&',
                message
        ));
    }

    public static void sendToAlOnlinePlayerMessage(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendMessage(onlinePlayer, message);
        }
    }
}

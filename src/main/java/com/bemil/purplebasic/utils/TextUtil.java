package com.bemil.purplebasic.utils;

import org.bukkit.ChatColor;

public class TextUtil {
    public static String build(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static String build(char str, String text) {
        return ChatColor.translateAlternateColorCodes(str, text);
    }
}

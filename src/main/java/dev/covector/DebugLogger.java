package dev.covector.cmcminigames;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

public class DebugLogger
{
    private static int LOG_LEVEL = 0;

    public static void log(String message, int level) {
        if (level <= LOG_LEVEL) {
            Bukkit.broadcastMessage(ChatColor.RED + "[DEBUG] - " + ChatColor.RESET + message);
        }
    }

    public static void setLogLevel(int level) {
        LOG_LEVEL = level;
    }

    public static boolean willLog(int level) {
        return level <= LOG_LEVEL;
    }
}
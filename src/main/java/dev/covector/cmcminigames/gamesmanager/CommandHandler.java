package dev.covector.cmcminigames.gamesmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import dev.covector.cmcminigames.DebugLogger;

public class CommandHandler implements CommandExecutor {    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;

        if (args[0].equals("reload")) {
            GamesManager.getInstance().loadMaps();
            sender.sendMessage(ChatColor.GREEN + "Reloaded maps!");
            return true;
        } else if (args[0].equals("leave")) {
            return GamesManager.getInstance().leave((Player) sender);
        }

        if (args.length < 2) return false;

        if (args[0].equals("join")) {
            if (sender instanceof Player) {
                String gameName = args[1];
                String error = GamesManager.getInstance().queue((Player) sender, gameName);
                if (error != null) {
                    sender.sendMessage(ChatColor.RED + error);
                    return false;
                }
                sender.sendMessage(ChatColor.GREEN + "You have joined the queue for " + gameName + "!");
                return true;
            } else {
                sender.sendMessage("You must be a player to join a game!");
                return false;
            }
        } else if (args[0].equals("start")) {
            if (args.length < 3) return false;
            String gameName = args[1];
            String mapName = args[2];
            List<String> gameArgs = (new ArrayList<>(Arrays.asList(args))).subList(3, args.length);
            String error = GamesManager.getInstance().startGame(gameName, mapName, gameArgs);
            if (error != null) {
                sender.sendMessage(ChatColor.RED + error);
                return false;
            }
            return true;
        } else if (args[0].equals("debug")) {
            int logLevel = Integer.parseInt(args[1]);
            DebugLogger.setLogLevel(logLevel);
            sender.sendMessage(ChatColor.GREEN + "Set log level to " + logLevel + "!");
            return true;
        } else if (args[0].equals("list")) {
            if (args.length < 2) return false;
            if (args[1].equals("queue")) {
                GamesManager.getInstance().listQueues((Player) sender);
                return true;
            } else if (args[1].equals("games")) {
                GamesManager.getInstance().listGames((Player) sender);
                return true;
            }
        }

        return false;
    }
}

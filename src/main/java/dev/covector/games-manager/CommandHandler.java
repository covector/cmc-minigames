package dev.covector.cmcminigames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandHandler implements CommandExecutor {    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
        }

        return false;
    }
}

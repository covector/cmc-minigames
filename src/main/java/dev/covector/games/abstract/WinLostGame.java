package dev.covector.cmcminigames.abs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.UUID;

import dev.covector.cmcminigames.Game;

public abstract class WinLostGame extends Game {
    public void win(UUID winnerUUID) {
        String winnerName;
        if (Bukkit.getPlayer(winnerUUID) != null) {
            winnerName = Bukkit.getPlayer(winnerUUID).getName();
        } else {
            winnerName = Bukkit.getOfflinePlayer(winnerUUID).getName();
        }
        
        Bukkit.broadcastMessage(ChatColor.GREEN + winnerName + " has won!");
        super.end();
    }

    public void win(TeamGame.Team team) {
        Bukkit.broadcastMessage(ChatColor.GREEN + team.teamName + " has won!");
        super.end();
    }
}
package dev.covector.cmcminigames.abs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import dev.covector.cmcminigames.Game;

public abstract class WinLostGame extends Game {
    public void win(Player player) {
        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has won!");
        super.end();
    }

    public void win(TeamGame.Team team) {
        Bukkit.broadcastMessage(ChatColor.GREEN + team.teamName + " has won!");
        super.end();
    }
}
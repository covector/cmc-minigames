package dev.covector.cmcminigames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.UUID;

public class Utils
{
    public static void distributePlayers(List<Player> players, List<Location> locations) {
        List<Player> shuffledPlayers = new ArrayList<Player>(players);
        Collections.shuffle(shuffledPlayers);
        List<Location> shuffledLocations = new ArrayList<Location>(locations);
        Collections.shuffle(shuffledLocations);
    
        for (int i = 0; i < shuffledPlayers.size(); i++) {
            shuffledPlayers.get(i).teleport(shuffledLocations.get(i % shuffledLocations.size()));
        }
    }

    public static String getPlayerNameByUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer != null) {
                return offlinePlayer.getName();
            } else {
                return "Unknown Player";
            }
        } else {
            return player.getName();
        }
    }
}
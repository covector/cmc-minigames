package dev.covector.cmcminigames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.UUID;

public class Utils
{
    public static void distributePlayers(List<Player> players, List<Location> locations) {
        List<Player> shuffledPlayers = new ArrayList(players);
        Collections.shuffle(shuffledPlayers);
        List<Location> shuffledLocations = new ArrayList(locations);
        Collections.shuffle(shuffledLocations);
    
        for (int i = 0; i < shuffledPlayers.size(); i++) {
            shuffledPlayers.get(i).teleport(shuffledLocations.get(i % shuffledLocations.size()));
        }
    }

    public static String getPlayerNameByUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return Bukkit.getOfflinePlayer(uuid).getName();
        } else {
            return player.getName();
        }
    }
}
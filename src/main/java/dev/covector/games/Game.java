package dev.covector.cmcminigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public abstract class Game {
    // DON'T OVERRIDE THESE
    private UUID id;
    protected List<UUID> playerUUIDs;
    protected MapInfo mapInfo;

    public void init(UUID id, List<UUID> playerUUIDs, MapInfo mapInfo) {
        this.id = id;
        this.playerUUIDs = playerUUIDs;
        this.mapInfo = mapInfo;
    }

    public void end() {
        GameEndEvent event = new GameEndEvent(id);
        Bukkit.getPluginManager().callEvent(event);
    }

    public List<UUID> getPlayerUUIDs() { // will also return offline players
        return playerUUIDs;
    }

    public List<Player> getOnlinePlayers() { // will only return online players
        List<Player> onlinePlayers = new ArrayList<Player>();
        for (UUID uuid : playerUUIDs) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public boolean playerIsInGame(Player player) {
        return playerUUIDs.contains(player.getUniqueId());
    }

    public boolean playerIsInGame(UUID playerUUID) {
        return playerUUIDs.contains(playerUUID);
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    // ONLY OVERRIDE THESE
    public abstract boolean start(List<String> args);
    public abstract String getArgsSyntax();
}
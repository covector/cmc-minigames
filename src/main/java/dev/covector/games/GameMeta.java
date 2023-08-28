package dev.covector.cmcminigames;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class GameMeta 
{
    public final String mapName;
    public final Location lobbyLocation;
    public final Location spectatorLocation;
    public final List<Location> spawnLocations;
    public final ConfigurationSection extraInfo;

    public GameMeta(String mapName, Location lobbyLocation, Location spectatorLocation, List<Location> spawnLocations, ConfigurationSection extraInfo) {
        this.mapName = mapName;
        this.lobbyLocation = lobbyLocation;
        this.spectatorLocation = spectatorLocation;
        this.spawnLocations = spawnLocations;
        this.extraInfo = extraInfo;
    }
}

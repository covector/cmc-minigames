package dev.covector.cmcminigames.games;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class MapInfo 
{
    public final String mapName;
    public final Location spectatorLocation;
    public final Location lobbyLocation;
    public final List<Location> spawnLocations;
    public final ConfigurationSection extraInfo;

    public MapInfo(String mapName, Location spectatorLocation, Location lobbyLocation, List<Location> spawnLocations, ConfigurationSection extraInfo) {
        this.mapName = mapName;
        this.spectatorLocation = spectatorLocation;
        this.lobbyLocation = lobbyLocation;
        this.spawnLocations = spawnLocations;
        this.extraInfo = extraInfo;
    }
}

package dev.covector.cmcminigames;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class MapInfo 
{
    public final String mapName;
    public final Location spectatorLocation;
    public final List<Location> spawnLocations;
    public final ConfigurationSection extraInfo;

    public MapInfo(String mapName, Location spectatorLocation, List<Location> spawnLocations, ConfigurationSection extraInfo) {
        this.mapName = mapName;
        this.spectatorLocation = spectatorLocation;
        this.spawnLocations = spawnLocations;
        this.extraInfo = extraInfo;
    }
}

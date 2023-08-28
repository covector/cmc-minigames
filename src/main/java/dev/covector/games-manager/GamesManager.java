package dev.covector.cmcminigames;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class GamesManager implements Listener {
    public static GamesManager manager;
    private HashMap<UUID, Game> activeGames = new HashMap<UUID, Game>();
    private HashMap<String, List<Player>> queuingPlayers = new HashMap<String, List<Player>>();
    private HashMap<UUID, Game> playerGameMap = new HashMap<UUID, Game>();
    private HashMap<String, HashMap<String, GameMeta>> gameMetaMap = new HashMap<String, HashMap<String, GameMeta>>();

    public static GamesManager getInstance() {
        if (manager == null) {
            manager = new GamesManager();
        }
        return manager;
    }
    
    public String startGame(String gameName, String mapName, List<String> gameArgs) {
        String gameOfficialName = GameRegistry.getGameName(gameName);
        if (gameOfficialName == null) {
            return "Game not found!";
        }
        if (!queuingPlayers.containsKey(gameOfficialName)) {
            return "No players in queue!";
        }
        Game game = GameRegistry.initGame(gameName);

        UUID gameID = UUID.randomUUID();
        List<UUID> players = new ArrayList<UUID>();
        for (Player player : queuingPlayers.get(gameOfficialName)) {
            if (player.isOnline()) {
                players.add(player.getUniqueId());
                playerGameMap.put(player.getUniqueId(), game);
            }
        }
        if (gameMetaMap.get(gameOfficialName) == null) {
            return "Game not configured!";
        }
        GameMeta gameMeta = gameMetaMap.get(gameOfficialName).get(mapName);
        if (gameMeta == null) {
            return "Map not found!";
        }
    
        game.init(gameID, players, gameMeta);

        if (!game.start(gameArgs)) {
            return "Game failed to start!";
        }
        
        activeGames.put(gameID, game);
        queuingPlayers.remove(gameOfficialName);

        return null;
    }

    public String queue(Player player, String gameName) {
        if (playerGameMap.containsKey(player.getUniqueId())) {
            return "You are already in a game!";
        }
        String gameOfficialName = GameRegistry.getGameName(gameName);
        if (!GameRegistry.hasGame(gameOfficialName)) {
            return "Game not found!";
        }
        if (queuingPlayers.containsKey(gameOfficialName) && queuingPlayers.get(gameOfficialName).contains(player)) {
            return "You are already in the queue!";
        }
        if (queuingPlayers.containsKey(gameOfficialName)) {
            queuingPlayers.get(gameOfficialName).add(player);
        } else {
            queuingPlayers.put(gameOfficialName, Arrays.asList(player));
        }
        return null;
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        Game game = activeGames.get(event.getId());
        for (UUID playerUUID : game.getPlayerUUIDs()) {
            playerGameMap.remove(playerUUID);
        }
        activeGames.remove(event.getId());
    }

    public void loadMaps() {
        // load maps.yml
        File customConfigFile = new File(CMCMinigames.plugin.getDataFolder(), "maps.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            CMCMinigames.plugin.saveResource("maps.yml", false);
         }

        FileConfiguration  customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // load maps into gameMetaMap
        for (String gameKey : customConfig.getKeys(false)) {
            String gameOfficialName = GameRegistry.getGameName(gameKey);
            if (gameOfficialName == null) {
                Bukkit.getLogger().warning("Game " + gameKey + " not found!");
                continue;
            }
            if (gameMetaMap.get(gameOfficialName) == null) {
                gameMetaMap.put(gameOfficialName, new HashMap<String, GameMeta>());
            }
            ConfigurationSection section = customConfig.getConfigurationSection(gameKey);
            for (String mapKey : section.getKeys(false)) {
                ConfigurationSection mapSection = section.getConfigurationSection(mapKey);
                String mapName = mapKey;
                World world = Bukkit.getWorld(mapSection.getString("world"));
                if (world == null) {
                    Bukkit.getLogger().warning("World " + mapSection.getString("world") + " not found!");
                    continue;
                }
                Location lobbyLocation = parseVector(mapSection.getString("lobby")).toLocation(world);
                if (lobbyLocation == null) {
                    Bukkit.getLogger().warning("Invalid lobby location for map " + mapName + "!");
                    continue;
                }
                Location spectatorLocation = parseVector(mapSection.getString("spectator")).toLocation(world);
                if (spectatorLocation == null) {
                    Bukkit.getLogger().warning("Invalid spectator location for map " + mapName + "!");
                    continue;
                }
                List<Location> spawnLocations = new ArrayList<Location>();
                for (String spawnLocationString : mapSection.getStringList("spawns")) {
                    Location spawnLocation = parseVector(spawnLocationString).toLocation(world);
                    if (spawnLocation == null) {
                        Bukkit.getLogger().warning("Invalid spawn location for map " + mapName + "!");
                        continue;
                    }
                    spawnLocations.add(spawnLocation);
                }
                ConfigurationSection extraInfo = mapSection.getConfigurationSection("extra");

                
                gameMetaMap.get(gameOfficialName).put(mapName, new GameMeta(mapName, lobbyLocation, spectatorLocation, spawnLocations, extraInfo));
            }
        }
    }

    private Vector parseVector(String vector) {
        String[] vectorSplit = vector.split(",");
        if (vectorSplit.length != 3) {
            return null;
        }
        return new Vector(Double.parseDouble(vectorSplit[0]), Double.parseDouble(vectorSplit[1]), Double.parseDouble(vectorSplit[2]));
    }
}

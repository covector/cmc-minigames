package dev.covector.cmcminigames.gamesmanager;

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
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

import dev.covector.cmcminigames.CMCMinigames;
import dev.covector.cmcminigames.DebugLogger;
import dev.covector.cmcminigames.games.Game;
import dev.covector.cmcminigames.games.GameEndEvent;
import dev.covector.cmcminigames.games.MapInfo;

public class GamesManager implements Listener {
    public static GamesManager manager;
    private HashMap<UUID, Game> activeGames = new HashMap<UUID, Game>();
    private HashMap<String, ArrayList<Player>> queuingPlayers = new HashMap<String, ArrayList<Player>>();
    private HashMap<Player, String> playerQueuedGame = new HashMap<Player, String>();
    private HashMap<UUID, Game> playerGameMap = new HashMap<UUID, Game>();
    private HashMap<String, HashMap<String, MapInfo>> mapInfos;
    private HashMap<String, Location> lobbyLocations;

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
        Game game = GameRegistry.initGame(gameOfficialName);
        if (game == null) {
            return "Game not found!";
        }   

        UUID gameID = UUID.randomUUID();
        List<UUID> players = new ArrayList<UUID>();
        for (Player player : queuingPlayers.get(gameOfficialName)) {
            if (player.isOnline()) {
                players.add(player.getUniqueId());
                playerGameMap.put(player.getUniqueId(), game);
            }
            playerQueuedGame.remove(player);
        }
        if (mapInfos.get(gameOfficialName) == null) {
            return "Game not configured!";
        }
        MapInfo mapInfo = mapInfos.get(gameOfficialName).get(mapName);
        if (mapInfo == null) {
            return "Map not found!";
        }
    
        game.init(gameID, players, mapInfo);
        DebugLogger.log("game start called: " + game.getClass().getSimpleName(), 1);
        if (DebugLogger.willLog(1)) {
            for (UUID playerUUID : players) {
                DebugLogger.log("player: " + Bukkit.getPlayer(playerUUID).getName(), 1);
            }
        }
        DebugLogger.log("map: " + mapName, 1);

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
        if (playerQueuedGame.containsKey(player)) {
            return "You are already in the queue!";
        }
        String gameOfficialName = GameRegistry.getGameName(gameName);
        if (gameOfficialName == null) {
            return "Game not found!";
        }
        if (queuingPlayers.containsKey(gameOfficialName)) {
            queuingPlayers.get(gameOfficialName).add(player);
        } else {
            queuingPlayers.put(gameOfficialName, new ArrayList<>(Arrays.asList(player)));
        }
        playerQueuedGame.put(player, gameOfficialName);
        // tp player to lobby
        player.teleport(lobbyLocations.get(gameOfficialName));
        player.setGameMode(GameMode.ADVENTURE);
        DebugLogger.log(player.getName() + " queued in " + gameOfficialName, 1);
        return null;
    }

    public boolean leave(Player player) {
        // leave game
        if (playerGameMap.containsKey(player.getUniqueId())) {
            Game game = playerGameMap.get(player.getUniqueId());
            game.removePlayer(player);
            playerGameMap.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "You have left the game!");
            return true;
        }

        // leave queue
        if (playerQueuedGame.containsKey(player)) {
            String gameOfficialName = playerQueuedGame.get(player);
            queuingPlayers.get(gameOfficialName).remove(player);
            playerQueuedGame.remove(player);
            player.sendMessage(ChatColor.GREEN + "You have left the queue!");
            return true;
        }
        
        player.sendMessage(ChatColor.RED + "You are not in a game or queue!");
        return false;
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        Game game = activeGames.get(event.getId());
        for (UUID playerUUID : game.getPlayerUUIDs()) {
            playerGameMap.remove(playerUUID);
        }
        for (Player player : game.getOnlinePlayers()) {
            // teleport player to lobby
            player.teleport(game.getMapInfo().lobbyLocation);
            player.setGameMode(GameMode.ADVENTURE);
        }
        activeGames.remove(event.getId());
        DebugLogger.log("game ended: " + game.getClass().getSimpleName(), 1);
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(this, CMCMinigames.plugin);
    }

    public void unregisterListeners() {
        GameEndEvent.getHandlerList().unregister(this);
    }

    public void forceEndAllGames() {
        for (Game game : activeGames.values()) {
            game.forceEnd();
        }
    }

    public void listQueues(Player player) {
        player.sendMessage(ChatColor.GREEN + "Queues:");
        for (String gameOfficialName : queuingPlayers.keySet()) {
            player.sendMessage(ChatColor.GREEN + gameOfficialName + ": ");
            for (Player queuedPlayer : queuingPlayers.get(gameOfficialName)) {
                player.sendMessage(ChatColor.GREEN + "    - " + queuedPlayer.getName());
            }
        }
    }

    public void listGames(Player player) {
        player.sendMessage(ChatColor.GREEN + "Games:");
        for (Game game : activeGames.values()) {
            player.sendMessage(ChatColor.GREEN + game.getClass().getSimpleName() + ":");
            for (Player gamePlayer : game.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GREEN + "    - " + gamePlayer.getName());
            }
        }
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

        // load maps into mapInfo
        mapInfos = new HashMap<String, HashMap<String, MapInfo>>();
        lobbyLocations = new HashMap<String, Location>();
        for (String gameKey : customConfig.getKeys(false)) {
            String gameOfficialName = GameRegistry.getGameName(gameKey);
            if (gameOfficialName == null) {
                Bukkit.getLogger().warning("Game " + gameKey + " not found!");
                continue;
            }
            if (mapInfos.get(gameOfficialName) == null) {
                mapInfos.put(gameOfficialName, new HashMap<String, MapInfo>());
            }
            ConfigurationSection section = customConfig.getConfigurationSection(gameKey);

            Location lobbyLocation = parseVector(section.getString("lobby")).toLocation(Bukkit.getWorld(section.getString("lobby-world")));
            if (lobbyLocation == null) {
                Bukkit.getLogger().warning("Invalid lobby location for game " + gameOfficialName + "!");
                continue;
            }

            lobbyLocations.put(gameOfficialName, lobbyLocation);

            ConfigurationSection mapsSection = section.getConfigurationSection("maps");
            if (mapsSection == null) {
                Bukkit.getLogger().warning("No maps found for game " + gameOfficialName + "!");
                continue;
            }
            for (String mapKey : mapsSection.getKeys(false)) {
                ConfigurationSection mapSection = mapsSection.getConfigurationSection(mapKey);
                String mapName = mapKey;
                World world = Bukkit.getWorld(mapSection.getString("world"));
                if (world == null) {
                    Bukkit.getLogger().warning("World " + mapSection.getString("world") + " not found!");
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

                
                mapInfos.get(gameOfficialName).put(mapName, new MapInfo(mapName, spectatorLocation, lobbyLocation, spawnLocations, extraInfo));

                DebugLogger.log("Loaded map " + mapName + " for game " + gameOfficialName, 1);
                DebugLogger.log("Spectator location: " + spectatorLocation.toString(), 1);
                DebugLogger.log("Spawn locations: " + spawnLocations.toString(), 1);
                DebugLogger.log("Extra info: " + (extraInfo == null ? "null" : extraInfo.toString()), 1);
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

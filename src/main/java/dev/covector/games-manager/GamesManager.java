package dev.covector.cmcminigames;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;

public class GamesManager implements Listener {
    public static GamesManager manager;
    private HashMap<UUID, Game> activeGames = new HashMap<UUID, Game>();
    private HashMap<String, List<Player>> queuingPlayers = new HashMap<String, List<Player>>();
    private HashMap<UUID, Game> playerGameMap = new HashMap<UUID, Game>();

    public static GamesManager getInstance() {
        if (manager == null) {
            manager = new GamesManager();
        }
        return manager;
    }
    
    public String startGame(String gameName, String mapName, List<String> gameArgs) {
        Game game = GameRegistry.initGame(gameName);

        if (game != null) {
            return "Game not found!";
        }

        UUID gameID = UUID.randomUUID();
        // game.init(gameID, players);

        // if (!game.start()) {
        //     return "Game failed to start!";
        // }
        
        activeGames.put(gameID, game);
        return null;
    }



    public String queue(Player player, String gameName) {
        if (playerGameMap.containsKey(player.getUniqueId())) {
            return "You are already in a game!";
        }
        if (!GameRegistry.hasGame(gameName)) {
            return "Game not found!";
        }
        if (queuingPlayers.containsKey(gameName) && queuingPlayers.get(gameName).contains(player)) {
            return "You are already in the queue!";
        }
        if (queuingPlayers.containsKey(gameName)) {
            queuingPlayers.get(gameName).add(player);
        } else {
            queuingPlayers.put(gameName, Arrays.asList(player));
        }
        return null;
    }


    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        activeGames.remove(event.getId());
    }
}

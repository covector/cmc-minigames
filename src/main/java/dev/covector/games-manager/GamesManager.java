package dev.covector.cmcminigames;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;

public class GameManager implements Listener {
    public static GameManager manager;
    private HashMap<UUID, Game> activeGames = new HashMap<UUID, Game>();
    private HashMap<String, List<Player>> waitingPlayers = new HashMap<String, List<Player>>();

    public GameManager getInstance() {
        if (manager == null) {
            manager = new GameManager();
        }
        return manager;
    }
    
    public String initGame(String gameName) {
        Game game = GameRegistry.initGame(gameName);

        if (game != null) {
            return "Game not found!";
        }

        UUID gameID = UUID.randomUUID();
        game.init(gameID, players)

        if (!game.start()) {
            return "Game failed to start!";
        }
        
        activeGames.put(gameID, game);
        return null;
    }

    public void endGame(Game game) {
        activeGames.remove(game);
    }
}

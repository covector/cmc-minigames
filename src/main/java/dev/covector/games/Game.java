package dev.covector.cmcminigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class Game {
    // DON'T OVERRIDE THESE
    private UUID id;
    protected List<Player> players;
    protected GameMeta gameMeta;

    public void init(UUID id, List<Player> players) {
        this.id = id;
        this.players = players;
    }

    public void end() {
        GameEndEvent event = new GameEndEvent(id);
        Bukkit.getPluginManager().callEvent(event);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean playerIsInGame(Player player) {
        return players.contains(player);
    }

    public GameMeta getGameMeta() {
        return gameMeta;
    }

    // ONLY OVERRIDE THESE
    public abstract boolean start(List<String> args);
    public abstract String getArgsSyntax();
}
package dev.covector.cmcminigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Game {
    // DON'T OVERRIDE THESE
    private UUID id;
    private List<Player> players;
    private GameMeta gameMeta;

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

    public GameMeta getGameMeta() {
        return gameMeta;
    }

    // ONLY OVERRIDE THIS
    public abstract boolean start(List<Player> players, List<String> args);
}
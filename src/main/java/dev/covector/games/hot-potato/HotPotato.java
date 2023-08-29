package dev.covector.cmcminigames.hotpotato;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import dev.covector.cmcminigames.abs.WinLostGame;
import dev.covector.cmcminigames.Utils;
import dev.covector.cmcminigames.DebugLogger;

public class HotPotato extends WinLostGame {
    private HotPotatoListener listener;
    private ExplosionTimer timer;
    private HotPotatoItems items;
    private List<UUID> playerAliveUUIDs;
    private UUID potatoHolderUUID;
    private Mode mode;
    private boolean resetOnTag;
    private Random random = new Random();

    public HotPotato() {
        listener = new HotPotatoListener(this);
        timer = new ExplosionTimer(this);
        items = new HotPotatoItems();
    }

    public String getArgsSyntax() {
        return "<mode:SUSSY|CLASSIC> <round length:seconds> <reset on tag:true|false>";
    }

    @Override
    public boolean start(List<String> args) {
        // check if at least 2 players, if not return false
        if (playerUUIDs.size() < 2) {
            DebugLogger.log("Not enough players to start game!", 1);
            return false;
        }

        // parse args
        if (args.size() != 3) {
            DebugLogger.log("Invalid number of arguments!", 1);
            return false;
        }
        mode = Mode.valueOf(args.get(0));
        int roundLength = Integer.parseInt(args.get(1));
        boolean resetOnTag = Boolean.parseBoolean(args.get(2));
        if (mode == null || roundLength <= 0) {
            return false;
        }
        DebugLogger.log("Starting game with mode " + mode.toString() + " and round length " + String.valueOf(roundLength) + " seconds and reset on tag set to " + (resetOnTag ? "true" : "false"), 1);

        // start listener and timer
        listener.register();
        timer.startTimer(roundLength);

        // choose random player to hold potato
        potatoHolderUUID = playerUUIDs.get(random.nextInt(playerUUIDs.size()));
        DebugLogger.log("Chose " + Utils.getPlayerNameByUUID(potatoHolderUUID) + " to hold the potato", 1);
        // teleport players to spawns
        Utils.distributePlayers(getOnlinePlayers(), mapInfo.spawnLocations);
        for (UUID playerUUID : playerUUIDs) {
            // add all players to alive list
            playerAliveUUIDs.add(playerUUID);

            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) continue;

            // give appropriate items
            if (playerUUID == potatoHolderUUID) {
                items.giveHotPotatoItems(player, mode);
            } else {
                items.giveSurvivorItems(player);
            }
        }

        return true;
    }

    public void passPotato(Player from, Player to) {
        potatoHolderUUID = to.getUniqueId();
        items.giveHotPotatoItems(to, mode);
        items.giveSurvivorItems(from);
        if (resetOnTag) {
            timer.resetTimer();
        }
        if (DebugLogger.willLog(1))
            DebugLogger.log(Utils.getPlayerNameByUUID(from.getUniqueId()) + " passed the potato to " + Utils.getPlayerNameByUUID(to.getUniqueId()), 1);
    }

    public void removePlayer(Player player) {
        playerAliveUUIDs.remove(player.getUniqueId());
        hotPotatoGameEndCheck();
        DebugLogger.log(Utils.getPlayerNameByUUID(player.getUniqueId()) + " has been removed from the game", 1);
    }

    public void setSpectator(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(mapInfo.spectatorLocation);
        DebugLogger.log(Utils.getPlayerNameByUUID(player.getUniqueId()) + " has been set to spectator", 1);
    }

    public void gracePeriodEnd() {
        for (UUID playerUUID : playerUUIDs) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(ChatColor.RED + "Grace period has ended!");
            }
        }
        DebugLogger.log("Grace period has ended", 1);
    }

    public boolean isGracePeriod() {
        return timer.isGracePeriod();
    }

    public void roundEnd() {
        Player potatoHolder = Bukkit.getPlayer(potatoHolderUUID);

        // lil bro disconnected
        if (potatoHolder == null) {
            for (UUID playerUUID : playerUUIDs) {
                Player player = Bukkit.getPlayer(playerUUID);
                if (player != null) {
                    player.sendMessage(ChatColor.RED + potatoHolder.getName() + " tried to escape the explosion by logging out. As a result, they blew up in real life.");
                }
            }
            hotPotatoGameEndCheck();
            DebugLogger.log("Potato holder disconnected, still count as exploded", 1);
            return;
        }

        // normal explosion sequence
        Location explosionLocation = potatoHolder.getLocation().clone().add(0, 1, 0);
        // kill potato holder
        potatoHolder.setHealth(0);
        playerAliveUUIDs.remove(potatoHolderUUID);
        // spawn explosion and play sound
        explosionLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, explosionLocation, 64, 0, 0, 0, 1.5);
        explosionLocation.getWorld().playSound(explosionLocation, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

        for (UUID playerUUID : playerUUIDs) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(ChatColor.RED + potatoHolder.getName() + " has exploded!");
            }
        }

        hotPotatoGameEndCheck();
        DebugLogger.log("Potato exploded", 1);
    }

    public void nextRound() {
        // choose random player to hold potato
        potatoHolderUUID = playerAliveUUIDs.get(random.nextInt(playerAliveUUIDs.size()));
        items.giveHotPotatoItems(getPotatoHolder(), mode);
        DebugLogger.log("Chose " + Utils.getPlayerNameByUUID(potatoHolderUUID) + " to hold the potato", 1);
    }

    public void hotPotatoGameEndCheck() {
        if (playerAliveUUIDs.size() == 1) {
            super.win(playerAliveUUIDs.get(0));
            listener.unregister();
            timer.stopTimer();
            DebugLogger.log("Sent game end event", 1);
        }
    }

    public Player getPotatoHolder() {
        return Bukkit.getPlayer(potatoHolderUUID);
    }

    enum Mode {
        CLASSIC, 
        SUSSY
    }
}
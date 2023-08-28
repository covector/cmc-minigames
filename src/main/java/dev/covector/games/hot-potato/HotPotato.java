package dev.covector.cmcminigames.hotpotato;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Random;

import dev.covector.cmcminigames.abs.WinLostGame;
import dev.covector.cmcminigames.Utils;

public class HotPotato extends WinLostGame {
    private HotPotatoListener listener;
    private ExplosionTimer timer;
    private HotPotatoItems items;
    private List<Player> playersAlive;
    private Player potatoHolder;
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
        if (players.size() < 2) {
            return false;
        }

        // parse args
        if (args.size() != 3) {
            return false;
        }
        mode = Mode.valueOf(args.get(0));
        int roundLength = Integer.parseInt(args.get(1));
        boolean resetOnTag = Boolean.parseBoolean(args.get(2));
        if (mode == null || roundLength <= 0) {
            return false;
        }

        // start listener and timer
        listener.register();
        timer.startTimer(roundLength);

        // choose random player to hold potato
        potatoHolder = players.get(random.nextInt(players.size()));
        // teleport players to spawns
        Utils.distributePlayers(players, gameMeta.spawnLocations);
        for (Player player : players) {
            // add all players to alive list
            playersAlive.add(player);
            // give appropriate items
            if (player == potatoHolder) {
                items.giveHotPotatoItems(player, mode);
            } else {
                items.giveSurvivorItems(player);
            }
        }

        return true;
    }

    public void passPotato(Player from, Player to) {
        potatoHolder = to;
        items.giveHotPotatoItems(to, mode);
        items.giveSurvivorItems(from);
        if (resetOnTag) {
            timer.resetTimer();
        }
    }

    public void killPlayer(Player player) {
        player.setHealth(0);
        playersAlive.remove(player);
    }

    public void setSpectator(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(gameMeta.spectatorLocation);
    }

    public void gracePeriodEnd() {
        for (Player player : players) {
            player.sendMessage(ChatColor.RED + "Grace period has ended!");
        }
    }

    public boolean isGracePeriod() {
        return timer.isGracePeriod();
    }

    public void roundEnd() {
        Location explosionLocation = potatoHolder.getLocation().clone().add(0, 1, 0);
        // kill potato holder
        killPlayer(potatoHolder);
        // spawn explosion and play sound
        explosionLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, explosionLocation, 64, 0, 0, 0, 1.5);
        explosionLocation.getWorld().playSound(explosionLocation, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

        for (Player player : players) {
            player.sendMessage(ChatColor.RED + potatoHolder.getName() + " has exploded!");
        }
    }

    public void nextRound() {
        // choose random player to hold potato
        potatoHolder = playersAlive.get(random.nextInt(playersAlive.size()));
        items.giveHotPotatoItems(potatoHolder, mode);
    }

    public void hotPotatoGameEndCheck() {
        if (playersAlive.size() == 1) {
            super.win(playersAlive.get(0));
            listener.unregister();
            timer.stopTimer();
        }
    }

    public Player getPotatoHolder() {
        return potatoHolder;
    }

    enum Mode {
        CLASSIC, 
        SUSSY
    }
}
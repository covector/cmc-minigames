package dev.covector.cmcminigames.hotpotato;

import java.util.List;

import dev.covector.cmcminigames.abs.WinLostGame;

public class HotPotato extends WinLostGame {
    private HotpotatoListener listener;
    private ExplosionTimer timer;
    private HotPotatoItems items;
    private List<Player> playersAlive;
    private Player potatoHolder;

    public HotPotato() {
        listener = new HotpotatoListener(this);
        timer = new ExplosionTimer(this);
        items = new HotPotatoItems();
    }

    @Override
    public boolean start(List<String> args) {
        listener.register();

        // check if at least 2 players, if not return false
        // read mode (sussy or classic) and time limit
        // set random player as potato holder
        // set playersAlive to all players
        // start timer
        // give appropriate items to players

        return true;
    }

    public void passPotato(Player from, Player to) {
        // set potatoHolder to to
        // update items and effect status
    }

    public void killPlayer(Player player) {
        // kill player
        // remove player from playersAlive
    }

    public void playerRespawnAfterDeath(Player player) {
        // remove items
        // set gamemode to spectator
        // tp to spawn
    }

    public void hotPotatoGameEndCheck() {
        // if playersAlive.size() == 1, win(playersAlive.get(0))
    }

    public void hotPotatoGameEnd() {
        super.win(null);
        listener.unregister();
    }

    enum Mode {
        CLASSIC, 
        SUSSY
    }
}
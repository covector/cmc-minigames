package dev.covector.cmcminigames.hotpotato;

import org.bukkit.event.Listener;

import java.util.List;

import dev.covector.cmcminigames.CMCMinigames;

public class HotPotatoListener implements Listener {
    private HotPotato hotpotato;

    public HotPotatoListener(HotPotato hotpotato) {
        this.hotpotato = hotpotato;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, CMCMinigames.plugin);
    }

    public void playerHitPlayer(EntityDamageByEntityEvent event) {
        // check if players are both in game
        // check if attacker is holding potato
        // pass potato to attacked player
    }

    public void playerRespawn(PlayerRespawnEvent event) {
        // check if player is in game
        // call method in Hotpotato
    }

    public void playerLeave(PlayerQuitEvent event) {
        // call killPlayer
    }
}
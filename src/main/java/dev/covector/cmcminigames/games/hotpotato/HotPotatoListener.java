package dev.covector.cmcminigames.games.hotpotato;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;

import dev.covector.cmcminigames.CMCMinigames;

public class HotPotatoListener implements Listener {
    private HotPotato hotpotato;

    public HotPotatoListener(HotPotato hotpotato) {
        this.hotpotato = hotpotato;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, CMCMinigames.plugin);
    }

    @EventHandler
    public void playerHitPlayer(EntityDamageByEntityEvent event) {
        // check for all conditions
        if (hotpotato.isGracePeriod()) return;
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        Player attacker = (Player) event.getDamager();
        Player attacked = (Player) event.getEntity();
        if (attacker == attacked) return;
        if (event.getDamage() < 0.01) return;
        if (!hotpotato.playerIsInGame(attacker) || !hotpotato.playerIsInGame(attacked)) return;
        if (hotpotato.getPotatoHolder() != attacker) return;

        // pass potato to attacked player
        hotpotato.passPotato(attacker, attacked);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        // cancel fall damage if player is in game
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (e.getCause() == DamageCause.FALL){
            if (hotpotato.playerIsInGame(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event) {
        // check for all conditions
        if (!hotpotato.playerIsInGame(event.getPlayer())) return;
        
        // set player to spectator
        hotpotato.setSpectator(event.getPlayer());
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        // check for all conditions
        if (!hotpotato.playerIsInGame(event.getPlayer())) return;
        
        // kill player
        hotpotato.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void playerJoin(Player player) {
        // check for all conditions
        if (!hotpotato.playerIsInGame(player)) return;
        
        // set player to spectator
        hotpotato.setSpectator(player);
    }

    public void unregister() {
        PlayerQuitEvent.getHandlerList().unregister(this);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);
    }
}
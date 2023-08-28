package dev.covector.cmcminigames.hotpotato;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import dev.covector.cmcminigames.CMCMinigames;

public class ExplosionTimer extends BukkitRunnable {
    private HotPotato hotpotato;

    public ExplosionTimer(HotPotato hotpotato) {
        this.hotpotato = hotpotato;
    }

    public void startTimer() {
        runTaskTimer(CMCMinigames.plugin, 0, 20);
    }

    @Override
    public void run() {
        // count down and kill player
    }
}
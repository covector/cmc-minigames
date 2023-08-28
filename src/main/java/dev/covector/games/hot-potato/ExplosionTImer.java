package dev.covector.cmcminigames.hotpotato;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import dev.covector.cmcminigames.CMCMinigames;

public class ExplosionTimer extends BukkitRunnable {
    private HotPotato hotpotato;
    private int roundLength;
    private int gracePeriod = 10;
    private int postRoundPeriod = 5;
    private int countDown;
    private Period period;

    public ExplosionTimer(HotPotato hotpotato) {
        this.hotpotato = hotpotato;
    }

    public void startTimer(int roundLength) {
        this.roundLength = roundLength;
        countDown = roundLength;
        runTaskTimer(CMCMinigames.plugin, 0, 20);
    }

    public void stopTimer() {
        cancel();
    }

    public void resetTimer() {
         if (period == Period.ROUND) {
            countDown = roundLength;
        }
    }

    @Override
    public void run() {
        switch (period) {
            case GRACE_PERIOD:
                if (countDown == 0) {
                    hotpotato.gracePeriodEnd();
                    period = Period.ROUND;
                    countDown = roundLength;
                } else {
                    countDown--;
                }
                break;
            case ROUND:
                if (countDown == 0) {
                    hotpotato.roundEnd();
                    period = Period.POST_ROUND;
                    countDown = postRoundPeriod;
                } else {
                    if (announceTime(countDown)) {
                        for (Player player: hotpotato.getPlayers()) {
                            player.sendMessage(ChatColor.RED + "The potato will explode in " + countDown + " seconds!");
                        }
                    }
                    countDown--;
                }
                break;
            case POST_ROUND:
                if (countDown == 0) {
                    hotpotato.nextRound();
                    period = Period.ROUND;
                    countDown = roundLength;
                } else {
                    countDown--;
                }
                break;
        }
    }

    public boolean isGracePeriod() {
        return period == Period.GRACE_PERIOD;
    }

    private boolean announceTime(int time) {
        return time <= 5 || time % 10 == 0;
    }

    private enum Period {
        GRACE_PERIOD,
        ROUND,
        POST_ROUND
    }
}
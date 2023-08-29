package dev.covector.cmcminigames;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

import dev.covector.cmcminigames.hotpotato.HotPotato;

public class GameRegistry
{
    private static List<Class> gameList = Arrays.asList(
        // Add games here
        HotPotato.class
    );

    public static Game initGame(String gameName) {
        Class gameClass = getGameClass(gameName);
        if (gameClass == null) {
            return null;
        }
        try {
            return (Game) gameClass.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static boolean hasGame(String gameName) {
        return getGameClass(gameName) != null;
    }

    public static String getGameName(String gameName) {
        return getGameClass(gameName).getSimpleName();
    }

    private static Class getGameClass(String gameName) {
        for (Class game : gameList) {
            if (game.getSimpleName().toLowerCase().equals(gameName.toLowerCase().replaceAll("_", "").replaceAll("-", ""))) {
                DebugLogger.log(gameName + " matched with " + game.getSimpleName(), 2);
                return game;
            }
        }
        return null;
    }
}

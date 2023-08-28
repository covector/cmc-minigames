package dev.covector.cmcminigames;

import java.util.Arrays;
import java.util.List;

import dev.covector.cmcminigames.hotpotato.HotPotato;

public class GameRegistry
{
    private static List<Class> gameList = Arrays.asList(
        // Add games here
        HotPotato.class
    );

    public static <T extends Game> Game initGame(String gameName) {
        Class<T> gameClass = getGameClass(gameName);
        if (gameClass == null) {
            return null;
        }
        try {
            return gameClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        finally {
            return null;
        }
    }

    public static boolean hasGame(String gameName) {
        return getGameClass(gameName) != null;
    }

    public static String getGameName(String gameName) {
        return getGameClass(gameName).getName();
    }

    private static <T extends Game> Class<T> getGameClass(String gameName) {
        for (Class<T> game : gameList) {
            if (game.getName().toLowerCase().equals(gameName.toLowerCase().replaceAll("_", "").replaceAll("-", ""))) {
                return game;
            }
        }
        return null;
    }
}

package dev.covector.cmcminigames;

import java.util.Arrays;
import java.util.List;

public static class GameRegistry
{
    List<Class<Game>> gameList = Arrays.asList(
        // Add games here
        HotPotato.class
    );

    public static Game initGame(String gameName) {
        Class<Game> gameClass = getGameClass(gameName);
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

    private static Class<Game> getGameClass(String gameName) {
        for (Class<Game> game : gameList) {
            if (game.getName().toLowerCase().equals(gameName.toLowerCase().replaceAll("_", "").replaceAll("-", ""))) {
                return game;
            }
        }
        return null;
    }
}

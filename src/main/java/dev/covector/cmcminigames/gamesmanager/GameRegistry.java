package dev.covector.cmcminigames.gamesmanager;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import dev.covector.cmcminigames.games.hotpotato.HotPotato;
import dev.covector.cmcminigames.DebugLogger;
import dev.covector.cmcminigames.games.Game;

public class GameRegistry
{
    private static List<Class<? extends Game>> gameList = Arrays.asList(
        // Add games here
        HotPotato.class
    );

    public static Game initGame(String gameName) {
        Class<? extends Game> gameClass = getGameClass(gameName);
        if (gameClass == null) {
            return null;
        }
        try {
            return (Game) gameClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasGame(String gameName) {
        return getGameClass(gameName) != null;
    }

    public static String getGameName(String gameName) {
        return getGameClass(gameName).getSimpleName();
    }

    private static Class<? extends Game> getGameClass(String gameName) {
        for (Class<? extends Game> game : gameList) {
            if (game.getSimpleName().toLowerCase().equals(gameName.toLowerCase().replaceAll("_", "").replaceAll("-", ""))) {
                DebugLogger.log(gameName + " matched with " + game.getSimpleName(), 2);
                return game;
            }
        }
        return null;
    }
}

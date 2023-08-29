package dev.covector.cmcminigames;

import org.bukkit.plugin.java.JavaPlugin;

public class CMCMinigames extends JavaPlugin{
    public static CMCMinigames plugin;

    @Override
    public void onEnable() {
        plugin = this;
        GamesManager.getInstance().loadMaps();
        CommandHandler commandHandler = new CommandHandler();
        getCommand("mg").setExecutor(commandHandler);
        GamesManager.getInstance().registerListeners();
        getLogger().info("CMC Minigames Plugin Activated!");
    }

    @Override
    public void onDisable() {
        GamesManager.getInstance().unregisterListeners();
        GamesManager.getInstance().forceEndAllGames();
        getCommand("mg").setExecutor(null);
        getLogger().info("CMC Minigames Plugin Deactivated!");
    }
}

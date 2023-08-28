package dev.covector.cmcminigames;

import org.bukkit.plugin.java.JavaPlugin;

public class CMCMinigames extends JavaPlugin{
    public static CMCMinigames plugin;

    @Override
    public void onEnable() {
        plugin = this;
        GamesManager.getInstance();
        CommandHandler commandHandler = new CommandHandler();
        getCommand("mg").setExecutor(commandHandler);
        getLogger().info("CMC Minigames Plugin Activated!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CMC Minigames Plugin Deactivated!");
    }
}

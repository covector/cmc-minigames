package dev.covector.cmcminigames;

public class CMCMinigames 
{
    public static CMCMinigames plugin;

    @Override
    public void onEnable() {
        plugin = this;
        GameManager.getInstance();
        CommandHandler commandHandler = new CommandHandler();
        getCommand("mg").setExecutor(commandHandler);
        getLogger().info("CMC Minigames Plugin Activated!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CMC Minigames Plugin Deactivated!");
    }
}

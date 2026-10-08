package lol.moruto.client;

import lol.moruto.client.config.ConfigManager;
import lol.moruto.client.module.ModulesManager;
import net.fabricmc.api.ModInitializer;

public class Core implements ModInitializer {
    public static Core instance;

    private ModulesManager modulesManager;
    private ConfigManager configManager;

    @Override
    public void onInitialize() {
        instance = this;

        modulesManager = new ModulesManager();

        configManager = new ConfigManager();
        configManager.load();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Core.instance != null) {
                configManager.save();
            }
        }));
    }

    public ModulesManager getModulesManager() {
        return modulesManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}

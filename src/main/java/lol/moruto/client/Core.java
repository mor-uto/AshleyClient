package lol.moruto.mod;

import lol.moruto.mod.backdoor.PacketManager;
import lol.moruto.mod.module.ModulesManager;
import net.fabricmc.api.ModInitializer;

public class Core implements ModInitializer {
    public static Core instance;

    private ModulesManager modulesManager;

    @Override
    public void onInitialize() {
        instance = this;

        System.setProperty("fabric.dfu.enabled", "false");
        System.setProperty("fabric.disableTelemetry", "true");

        modulesManager = new ModulesManager();
        PacketManager.init();
    }

    public ModulesManager getModulesManager() {
        return modulesManager;
    }
}

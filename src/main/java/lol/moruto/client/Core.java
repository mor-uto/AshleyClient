package lol.moruto.client;

import lol.moruto.client.config.ConfigManager;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.ModulesManager;
import lol.moruto.client.ui.ClickGUI;
import lol.moruto.client.ui.NotificationManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class Core implements ModInitializer {
    public static Core instance;

    private ModulesManager modulesManager;
    private ConfigManager configManager;

    private boolean rightShiftPressed;

    @Override
    public void onInitialize() {
        instance = this;

        modulesManager = new ModulesManager();
        configManager = new ConfigManager();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                for (Module module : getModulesManager().getEnabledModules()) {
                    module.onUpdate();
                }
            }

            long window = client.getWindow().getHandle();

            boolean pressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

            if (pressed && !rightShiftPressed) {
                if (client.currentScreen instanceof ClickGUI) {
                    client.setScreen(null);
                } else if (client.currentScreen == null) {
                    client.setScreen(new ClickGUI());
                }
            }

            rightShiftPressed = pressed;
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            context.drawText(client.textRenderer, "Ashley Client v1.0", 10, 5, -1, true);

            NotificationManager.render(context);

            for (Module module : Core.instance.getModulesManager().getModules()) {
                if (module.isToggled()) {
                    module.render(context);
                }
            }
        });
    }

    public ModulesManager getModulesManager() {
        return modulesManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
package lol.moruto.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lol.moruto.client.Core;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.ModuleSetting;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private final File configFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        configFile = new File(MinecraftClient.getInstance().runDirectory, "ashleyclient.json");
    }

    public void save() {
        JsonObject root = new JsonObject();
        JsonObject modules = new JsonObject();

        for (Module module : Core.instance.getModulesManager().getModules()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.isToggled());

            JsonObject settings = new JsonObject();

            for (ModuleSetting<?> setting : module.getSettings()) {
                Object value = setting.getValue();
                settings.add(setting.getName(), gson.toJsonTree(value));
            }

            moduleObject.add("settings", settings);
            modules.add(module.getName(), moduleObject);
        }

        root.add("modules", modules);

        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!configFile.exists()) return;

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject modules = root.getAsJsonObject("modules");

            if (modules == null) return;

            for (Module module : Core.instance.getModulesManager().getModules()) {
                if (!modules.has(module.getName())) continue;

                JsonObject moduleObject = modules.getAsJsonObject(module.getName());

                if (moduleObject.has("enabled")) {
                    boolean enabled = moduleObject.get("enabled").getAsBoolean();

                    if (enabled != module.isToggled()) {
                        module.toggle();
                    }
                }

                if (!moduleObject.has("settings")) continue;

                JsonObject settings = moduleObject.getAsJsonObject("settings");

                for (ModuleSetting<?> setting : module.getSettings()) {
                    if (!settings.has(setting.getName())) continue;

                    JsonObject settingValue = settings;
                    loadSetting(setting, settingValue.get(setting.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadSetting(ModuleSetting setting, com.google.gson.JsonElement element) {
        Object currentValue = setting.getValue();

        if (currentValue instanceof Boolean) {
            setting.setValue(element.getAsBoolean());
        } else if (currentValue instanceof Integer) {
            setting.setValue(element.getAsInt());
        } else if (currentValue instanceof Double) {
            setting.setValue(element.getAsDouble());
        } else if (currentValue instanceof Float) {
            setting.setValue(element.getAsFloat());
        } else if (currentValue instanceof Long) {
            setting.setValue(element.getAsLong());
        } else if (currentValue instanceof String) {
            setting.setValue(element.getAsString());
        }
    }
}
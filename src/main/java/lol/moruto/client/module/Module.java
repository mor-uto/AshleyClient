package lol.moruto.client.module;

import lol.moruto.client.module.impl.setting.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean toggled;
    private final KeybindSetting keybindSetting = new KeybindSetting("Keybind", -1);

    private final List<ModuleSetting<?>> settings = new ArrayList<>();
    private boolean expanded = false;

    public final MinecraftClient mc = MinecraftClient.getInstance();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.toggled = false;
        addSetting(keybindSetting);
    }

    public final void toggle() {
        toggled = !toggled;
        if (toggled) onEnable();
        else onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void render(DrawContext context) {}

    public int getKeyCode() { return keybindSetting.getKey(); }

    public void addSetting(ModuleSetting<?> setting) {
        settings.add(setting);
    }

    public List<ModuleSetting<?>> getSettings() {
        return settings;
    }

    public boolean hasSettings() {
        return !settings.isEmpty();
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public ModuleSetting<?> getSetting(String name) {
        for (ModuleSetting moduleSetting : settings) {
            if (moduleSetting.name.equals(name)) return moduleSetting;
        }

        return null;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isToggled() { return toggled; }
}

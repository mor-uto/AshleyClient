package lol.moruto.mod.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean toggled;
    private final int keyCode;

    private final List<ModuleSetting<?>> settings = new ArrayList<>();
    private boolean expanded = false;

    public final MinecraftClient mc = MinecraftClient.getInstance();

    public Module(String name, String description, Category category) {
        this(name, description, category, GLFW.GLFW_KEY_UNKNOWN);
    }

    public Module(String name, String description, Category category, int keyCode) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.toggled = false;
        this.keyCode = keyCode;
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

    public int getKeyCode() { return keyCode; }

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

    public void collapse() {
        this.expanded = false;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isToggled() { return toggled; }
}

package lol.moruto.mod.module;

import net.minecraft.client.gui.DrawContext;

public abstract class ModuleSetting<T> {
    protected final String name;
    protected T value;

    public ModuleSetting(String name, T defaultValue) {
        this.name = name;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public abstract String getDisplayString();

    public abstract void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY);

    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }
}
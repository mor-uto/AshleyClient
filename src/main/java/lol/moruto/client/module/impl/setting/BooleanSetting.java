package lol.moruto.mod.module.impl.setting;

import lol.moruto.mod.module.ModuleSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class BooleanSetting extends ModuleSetting<Boolean> {

    public BooleanSetting(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public String getDisplayString() {
        return name + ": " + (value ? "ON" : "OFF");
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;

        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int background = hovered ? 0xFF292929 : 0xFF202020;
        int border = hovered ? 0xFF454545 : 0xFF333333;

        context.fill(x, y, x + width, y + height, background);
        context.drawBorder(x, y, width, height, border);

        context.fill(x, y, x + 3, y + height, value ? 0xFF6C63FF : 0xFF444444);

        context.drawText(tr, name, x + 10, y + 9, 0xFFFFFFFF, false);

        int toggleWidth = 30;
        int toggleHeight = 14;

        int toggleX = x + width - toggleWidth - 10;
        int toggleY = y + (height - toggleHeight) / 2;

        int toggleBackground = value ? 0xFF6C63FF : 0xFF383838;

        context.fill(toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, toggleBackground);

        int knobSize = 10;
        int knobX = value ? toggleX + toggleWidth - knobSize - 2 : toggleX + 2;

        int knobY = toggleY + 2;

        context.fill(knobX, knobY, knobX + knobSize, knobY + knobSize, 0xFFFFFFFF);

        String state = value ? "ON" : "OFF";
        int stateWidth = tr.getWidth(state);

        context.drawText(tr, state, toggleX - stateWidth - 8, y + 9, value ? 0xFF8E88FF : 0xFF666666, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        value = !value;
        return true;
    }
}
package lol.moruto.mod.module.impl.setting;

import lol.moruto.mod.module.ModuleSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class NumberSetting extends ModuleSetting<Double> {
    private final double min, max, step;
    private double animatedValue;
    private boolean dragging;

    public NumberSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
        this.animatedValue = defaultValue;
    }

    @Override
    public String getDisplayString() {
        return name + ": " + String.format("%.2f", value);
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();

        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        animatedValue += (value - animatedValue) * 0.25;

        double percent = (animatedValue - min) / (max - min);
        percent = Math.max(0, Math.min(1, percent));

        int trackX = x + 8;
        int trackWidth = width - 16;
        int trackY = y + height - 7;

        int sliderX = trackX + (int) (percent * trackWidth);

        int background = hovered || dragging ? 0xFF292929 : 0xFF202020;

        context.fill(x, y, x + width, y + height, background);
        context.drawBorder(x, y, width, height, dragging ? 0xFF6C63FF : 0xFF333333);
        context.drawText(mc.textRenderer, getDisplayString(), x + 8, y + 6, 0xFFFFFFFF, false);
        context.fill(trackX, trackY, trackX + trackWidth, trackY + 3, 0xFF383838);
        context.fill(trackX, trackY, sliderX, trackY + 3, dragging ? 0xFF8178FF : 0xFF6C63FF);
        context.fill(sliderX - 3, trackY - 3, sliderX + 4, trackY + 6, dragging ? 0xFFFFFFFF : 0xFFCCCCCC);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        dragging = true;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return dragging && button == 0;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
            return true;
        }

        return false;
    }

    public void setValueFromMouseX(double mouseX, int x, int width) {
        double percent = (mouseX - x) / width;

        percent = Math.max(0, Math.min(1, percent));

        double newValue = min + (max - min) * percent;

        if (step > 0) {
            newValue = Math.round(newValue / step) * step;
        }

        value = Math.max(min, Math.min(max, newValue));
    }

    public boolean isDragging() {
        return dragging;
    }
}
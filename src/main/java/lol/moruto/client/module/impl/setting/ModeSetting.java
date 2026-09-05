package lol.moruto.client.module.impl.setting;

import lol.moruto.client.module.ModuleSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class ModeSetting extends ModuleSetting<String> {
    private final List<String> modes;
    private int currentIndex;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name, defaultMode);
        this.modes = List.of(modes);
        this.currentIndex = Math.max(this.modes.indexOf(defaultMode), 0);
    }

    public void nextMode() {
        if (modes.isEmpty()) return;
        currentIndex = (currentIndex + 1) % modes.size();
        value = getMode();
    }

    public void previousMode() {
        if (modes.isEmpty()) return;
        currentIndex = (currentIndex - 1 + modes.size()) % modes.size();
        value = getMode();
    }

    public String getMode() {
        return modes.isEmpty() ? "" : modes.get(currentIndex);
    }

    public String getDisplayString() {
        return name + ": " + getMode();
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

        context.fill(x, y, x + 3, y + height, 0xFF6C63FF);
        context.drawText(tr, name, x + 10, y + 5, 0xFFFFFFFF, false);

        String mode = getMode();
        int modeWidth = tr.getWidth(mode);

        context.drawText(tr, mode, x + width - modeWidth - 20, y + 5, hovered ? 0xFFFFFFFF : 0xFFBBBBBB, false);

        context.drawText(tr, "‹", x + width - 12, y + 5, hovered ? 0xFF6C63FF : 0xFF777777, false);

        if (modes.size() > 1) {
            int indicatorWidth = width - 20;
            int filledWidth = (int) ((double) (currentIndex + 1) / modes.size() * indicatorWidth);

            context.fill(x + 10, y + height - 4, x + 10 + indicatorWidth, y + height - 2, 0xFF383838);
            context.fill(x + 10, y + height - 4, x + 10 + filledWidth, y + height - 2, 0xFF6C63FF);
        }

        context.drawText(tr, "›", x + width - 7, y + 5, hovered ? 0xFF6C63FF : 0xFF777777, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            nextMode();
            return true;
        }

        if (button == 1) {
            previousMode();
            return true;
        }

        return false;
    }
}
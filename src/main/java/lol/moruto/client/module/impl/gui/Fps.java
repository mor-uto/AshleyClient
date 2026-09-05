package lol.moruto.client.module.impl.gui;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.client.gui.DrawContext;

public class Fps extends Module {
    public Fps() {
        super("Fps", "Shows you the game fps", Category.GUI);
    }

    @Override
    public void render(DrawContext context) {
        context.drawText(mc.textRenderer, "Fps: " + mc.getCurrentFps(), 4, mc.getWindow().getScaledHeight() - mc.textRenderer.fontHeight - 4, 0xFFFFFFFF, true);
    }
}
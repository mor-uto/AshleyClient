package lol.moruto.mod.module.impl.gui;

import lol.moruto.mod.Core;
import lol.moruto.mod.module.Category;
import lol.moruto.mod.module.Module;
import lol.moruto.mod.util.ColorUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayList extends Module {
    private final Map<Module, Float> moduleOffsets = new HashMap<>();

    public ArrayList() {
        super("ArrayList", "Renders the enabled mods", Category.GUI);
    }

    @Override
    public void render(DrawContext context) {
        List<Module> modules = Core.instance.getModulesManager().getModules();
        int screenWidth = mc.getWindow().getScaledWidth();
        int yOffset = 5;

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            String modName = module.getName();
            int modNameWidth = mc.textRenderer.getWidth(modName);

            int targetX = module.isToggled() ? screenWidth - modNameWidth - 15 : screenWidth + modNameWidth;

            float currentX = moduleOffsets.getOrDefault(module, (float)(screenWidth + modNameWidth));
            float animationSpeed = 8f;
            if (currentX < targetX) currentX = Math.min(currentX + animationSpeed, targetX);
            else if (currentX > targetX) currentX = Math.max(currentX - animationSpeed, targetX);

            moduleOffsets.put(module, currentX);

            boolean isVisible = module.isToggled() || currentX != targetX;
            if (isVisible) {
                int rainbowColor = ColorUtil.getRainbowColor(i * 0.1f);
                context.drawText(mc.textRenderer, "- " + modName, (int)currentX, yOffset, rainbowColor, true);
                yOffset += mc.textRenderer.fontHeight + 2;
            }
        }
    }
}

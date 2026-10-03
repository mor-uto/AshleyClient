package lol.moruto.client.ui;

import lol.moruto.client.Core;
import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.ModuleSetting;
import lol.moruto.client.module.impl.setting.KeybindSetting;
import lol.moruto.client.module.impl.setting.NumberSetting;
import lol.moruto.client.module.impl.setting.StringSetting;
import lol.moruto.client.ui.component.CategoryButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final StringSetting search = new StringSetting("Search", "");
    private Category selectedCategory = Category.MOVEMENT;

    private static final int WIDTH = 520, HEIGHT = 320, SIDEBAR_WIDTH = 125;
    private static final int MODULE_HEIGHT = 42, MODULE_SPACING = 7;
    private static final int SETTING_HEIGHT = 28, SETTING_SPACING = 3;

    private double scrollOffset, targetScrollOffset;

    public ClickGUI() {
        super(Text.literal("ClickGUI"));
        for (Category category : Category.values()) categoryButtons.add(new CategoryButton(this, 105, category));
    }

    private List<Module> getModules() {
        List<Module> modules = Core.instance.getModulesManager().getModulesByCategory(selectedCategory);
        String query = search.getValue().trim().toLowerCase();
        if (query.isEmpty()) return modules;

        List<Module> filtered = new ArrayList<>();
        for (Module module : modules)
            if (module.getName().toLowerCase().contains(query) || module.getDescription().toLowerCase().contains(query))
                filtered.add(module);
        return filtered;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int x = (width - WIDTH) / 2, y = (height - HEIGHT) / 2;
        context.fill(x, y, x + WIDTH, y + HEIGHT, 0xFF111111);
        context.drawBorder(x, y, WIDTH, HEIGHT, 0xFF303030);
        context.fill(x, y, x + SIDEBAR_WIDTH, y + HEIGHT, 0xFF151515);
        context.fill(x + SIDEBAR_WIDTH, y, x + SIDEBAR_WIDTH + 1, y + HEIGHT, 0xFF303030);


        context.drawText(textRenderer, "Ashley", x + 15, y + 14, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Client", x + 15, y + 26, 0xFF777777, false);

        int categoryY = y + 50;
        for (CategoryButton button : categoryButtons) {
            button.setPosition(x + 10, categoryY);
            if (button.getCategory() == selectedCategory) {
                context.fill(x + 7, categoryY - 1, x + SIDEBAR_WIDTH - 7, categoryY + 21, 0xFF303030);
                context.fill(x + 7, categoryY - 1, x + 10, categoryY + 21, 0xFF6C63FF);
            }
            button.render(context, mouseX, mouseY, delta);
            categoryY += 28;
        }

        int contentX = x + SIDEBAR_WIDTH + 20, contentWidth = WIDTH - SIDEBAR_WIDTH - 35;
        context.drawText(textRenderer, selectedCategory.name().toLowerCase(), contentX, y + 15, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Modules", contentX, y + 29, 0xFF666666, false);

        search.render(context, contentX, y + 40, contentWidth, 28, mouseX, mouseY);

        int contentTop = y + 73, contentBottom = y + HEIGHT - 10;
        context.enableScissor(contentX, contentTop, x + WIDTH - 10, contentBottom);

        scrollOffset += (targetScrollOffset - scrollOffset) * 0.25;
        int moduleY = contentTop + 5 - (int) scrollOffset;
        List<Module> modules = getModules();

        for (Module module : modules) {
            renderModuleButton(context, module, contentX, moduleY, contentWidth, mouseX, mouseY);
            moduleY += MODULE_HEIGHT + MODULE_SPACING;

            if (module.isExpanded() && module.hasSettings()) {
                moduleY = renderSettings(context, module, contentX, moduleY, contentWidth, mouseX, mouseY);
                moduleY += MODULE_SPACING;
            }
        }

        context.disableScissor();

        int totalHeight = calculateContentHeight(modules), visibleHeight = contentBottom - contentTop;
        if (totalHeight > visibleHeight) {
            int scrollbarHeight = Math.max(20, visibleHeight * visibleHeight / totalHeight);
            int maxScroll = totalHeight - visibleHeight;
            int scrollbarY = contentTop + (int) ((scrollOffset / maxScroll) * (visibleHeight - scrollbarHeight));
            context.fill(x + WIDTH - 7, contentTop, x + WIDTH - 4, contentBottom, 0xFF202020);
            context.fill(x + WIDTH - 7, scrollbarY, x + WIDTH - 4, scrollbarY + scrollbarHeight, 0xFF666666);
        }
    }

    private int calculateContentHeight(List<Module> modules) {
        int height = 5;
        for (Module module : modules) {
            height += MODULE_HEIGHT + MODULE_SPACING;
            if (module.isExpanded() && module.hasSettings())
                height += module.getSettings().size() * (SETTING_HEIGHT + SETTING_SPACING) + MODULE_SPACING;
        }
        return height;
    }

    private void renderModuleButton(DrawContext context, Module module, int x, int y, int width, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + MODULE_HEIGHT;
        boolean enabled = module.isToggled();
        int background = enabled ? (hovered ? 0xFF5149A3 : 0xFF40388A) : (hovered ? 0xFF292929 : 0xFF202020);

        context.fill(x, y, x + width, y + MODULE_HEIGHT, background);
        context.drawBorder(x, y, width, MODULE_HEIGHT, enabled ? 0xFF6C63FF : 0xFF333333);
        context.drawText(textRenderer, module.getName(), x + 10, y + 6, 0xFFFFFFFF, false);
        context.drawText(textRenderer, module.getDescription(), x + 10, y + 22, 0xFF999999, false);

        String state = enabled ? "ON" : "OFF";
        int stateWidth = textRenderer.getWidth(state);
        context.drawText(textRenderer, state, x + width - stateWidth - 25, y + 8, enabled ? 0xFFFFFFFF : 0xFF666666, false);

        if (module.hasSettings()) {
            String arrow = module.isExpanded() ? "▲" : "▼";
            context.drawText(textRenderer, arrow, x + width - textRenderer.getWidth(arrow) - 9, y + 23, 0xFFAAAAAA, false);
        }
    }

    private int renderSettings(DrawContext context, Module module, int x, int y, int width, int mouseX, int mouseY) {
        for (ModuleSetting<?> setting : module.getSettings()) {
            setting.render(context, x, y, width, SETTING_HEIGHT, mouseX, mouseY);
            y += SETTING_HEIGHT + SETTING_SPACING;
        }
        return y;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - WIDTH) / 2, y = (height - HEIGHT) / 2;
        int contentX = x + SIDEBAR_WIDTH + 20, contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        if (search.mouseClicked(mouseX, mouseY, button)) return true;

        for (CategoryButton categoryButton : categoryButtons) categoryButton.mouseClicked(mouseX, mouseY, button);

        int contentTop = y + 73, contentBottom = y + HEIGHT - 10;
        if (mouseX < contentX || mouseX > contentX + contentWidth || mouseY < contentTop || mouseY > contentBottom)
            return super.mouseClicked(mouseX, mouseY, button);

        int moduleY = contentTop + 5 - (int) scrollOffset;

        for (Module module : getModules()) {
            if (mouseX >= contentX && mouseX <= contentX + contentWidth && mouseY >= moduleY && mouseY <= moduleY + MODULE_HEIGHT) {
                if (button == 0) {
                    module.toggle();
                    return true;
                }
                if (button == 1 && module.hasSettings()) {
                    module.setExpanded(!module.isExpanded());
                    clampScroll();
                    return true;
                }
            }

            moduleY += MODULE_HEIGHT + MODULE_SPACING;

            if (module.isExpanded() && module.hasSettings()) {
                for (ModuleSetting<?> setting : module.getSettings()) {
                    if (mouseX >= contentX && mouseX <= contentX + contentWidth && mouseY >= moduleY && mouseY <= moduleY + SETTING_HEIGHT && setting.mouseClicked(mouseX, mouseY, button))
                        return true;
                    moduleY += SETTING_HEIGHT + SETTING_SPACING;
                }
                moduleY += MODULE_SPACING;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int x = (width - WIDTH) / 2, y = (height - HEIGHT) / 2;
        int contentX = x + SIDEBAR_WIDTH + 20, contentTop = y + 73, contentBottom = y + HEIGHT - 10;
        int contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        if (mouseX >= contentX && mouseX <= contentX + contentWidth && mouseY >= contentTop && mouseY <= contentBottom) {
            targetScrollOffset -= verticalAmount * 25;
            clampScroll();
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private void clampScroll() {
        int y = (height - HEIGHT) / 2;
        int visibleHeight = y + HEIGHT - 10 - (y + 73);
        int maxScroll = Math.max(0, calculateContentHeight(getModules()) - visibleHeight);
        targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScroll));
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryButton categoryButton : categoryButtons) categoryButton.mouseReleased(mouseX, mouseY, button);

        search.mouseReleased(mouseX, mouseY, button);

        for (Module module : getModules())
            for (ModuleSetting<?> setting : module.getSettings())
                setting.mouseReleased(mouseX, mouseY, button);

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int x = (width - WIDTH) / 2;
        int contentX = x + SIDEBAR_WIDTH + 20;
        int contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        for (Module module : getModules()) {
            if (!module.isExpanded() || !module.hasSettings()) continue;

            for (ModuleSetting<?> setting : module.getSettings()) {
                if (setting.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    if (setting instanceof NumberSetting numberSetting)
                        numberSetting.setValueFromMouseX(mouseX, contentX, contentWidth);
                    return true;
                }
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (search.keyPressed(keyCode, scanCode, modifiers)) {
            clampScroll();
            return true;
        }

        for (Module module : getModules()) {
            for (ModuleSetting<?> setting : module.getSettings()) {
                if (setting instanceof StringSetting stringSetting && stringSetting.keyPressed(keyCode, scanCode, modifiers))
                    return true;
                if (setting instanceof KeybindSetting keybindSetting && keybindSetting.keyPressed(keyCode, scanCode, modifiers))
                    return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (search.charTyped(chr, modifiers)) {
            targetScrollOffset = scrollOffset = 0;
            return true;
        }

        for (Module module : getModules())
            for (ModuleSetting<?> setting : module.getSettings())
                if (setting instanceof StringSetting stringSetting && stringSetting.charTyped(chr, modifiers))
                    return true;

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        targetScrollOffset = scrollOffset = 0;
        search.setFocused(false);
    }
}

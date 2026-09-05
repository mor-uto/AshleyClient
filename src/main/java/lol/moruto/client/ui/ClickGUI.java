package lol.moruto.client.ui;

import lol.moruto.client.Core;
import lol.moruto.client.module.Category;
import lol.moruto.client.module.ModuleSetting;
import lol.moruto.client.module.impl.setting.NumberSetting;
import lol.moruto.client.ui.component.CategoryButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import lol.moruto.client.module.Module;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {

    private final List<CategoryButton> categoryButtons = new ArrayList<>();

    private Category selectedCategory = Category.MOVEMENT;

    private static final int WIDTH = 520;
    private static final int HEIGHT = 320;

    private static final int SIDEBAR_WIDTH = 125;

    private static final int MODULE_HEIGHT = 42;
    private static final int MODULE_SPACING = 7;

    private static final int SETTING_HEIGHT = 28;
    private static final int SETTING_SPACING = 3;

    private double scrollOffset = 0;
    private double targetScrollOffset = 0;

    public ClickGUI() {
        super(Text.literal("ClickGUI"));

        for (Category category : Category.values()) {
            categoryButtons.add(new CategoryButton(this, 105, category));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int x = (this.width - WIDTH) / 2;
        int y = (this.height - HEIGHT) / 2;

        context.fill(x, y, x + WIDTH, y + HEIGHT, 0xFF111111);
        context.drawBorder(x, y, WIDTH, HEIGHT, 0xFF303030);

        context.fill(x, y, x + SIDEBAR_WIDTH, y + HEIGHT, 0xFF151515);
        context.fill(x + SIDEBAR_WIDTH, y, x + SIDEBAR_WIDTH + 1, y + HEIGHT, 0xFF303030);

        context.drawText(textRenderer, "Ashley", x + 15, y + 14, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Client", x + 15, y + 26, 0xFF777777, false);

        int categoryY = y + 50;

        for (CategoryButton categoryButton : categoryButtons) {
            categoryButton.setPosition(x + 10, categoryY);

            boolean selected = categoryButton.getCategory() == selectedCategory;

            if (selected) {
                context.fill(x + 7, categoryY - 1, x + SIDEBAR_WIDTH - 7, categoryY + 21, 0xFF303030);
                context.fill(x + 7, categoryY - 1, x + 10, categoryY + 21, 0xFF6C63FF);
            }

            categoryButton.render(context, mouseX, mouseY, delta);
            categoryY += 28;
        }

        int contentX = x + SIDEBAR_WIDTH + 20;
        int contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        context.drawText(textRenderer, selectedCategory.name(), contentX, y + 15, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Modules", contentX, y + 29, 0xFF666666, false);

        int contentTop = y + 50;
        int contentBottom = y + HEIGHT - 10;

        context.enableScissor(contentX, contentTop, x + WIDTH - 10, contentBottom);

        scrollOffset += (targetScrollOffset - scrollOffset) * 0.25;

        int moduleY = contentTop + 5 - (int) scrollOffset;

        List<Module> modules = Core.instance.getModulesManager().getModulesByCategory(selectedCategory);

        for (Module module : modules) {
            renderModuleButton(context, module, contentX, moduleY, contentWidth, mouseX, mouseY);

            moduleY += MODULE_HEIGHT + MODULE_SPACING;

            if (module.isExpanded() && module.hasSettings()) {
                moduleY = renderSettings(context, module, contentX, moduleY, contentWidth, mouseX, mouseY);
                moduleY += MODULE_SPACING;
            }
        }

        context.disableScissor();

        int totalHeight = calculateContentHeight(modules);
        int visibleHeight = contentBottom - contentTop;

        if (totalHeight > visibleHeight) {
            int scrollbarHeight = Math.max(20, (visibleHeight * visibleHeight) / totalHeight);
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

            if (module.isExpanded() && module.hasSettings()) {
                height += module.getSettings().size() * (SETTING_HEIGHT + SETTING_SPACING);
                height += MODULE_SPACING;
            }
        }

        return height;
    }

    private void renderModuleButton(DrawContext context, Module module, int x, int y, int width, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + MODULE_HEIGHT;

        boolean enabled = module.isToggled();

        int background;

        if (enabled) {
            background = hovered ? 0xFF5149A3 : 0xFF40388A;
        } else {
            background = hovered ? 0xFF292929 : 0xFF202020;
        }

        context.fill(x, y, x + width, y + MODULE_HEIGHT, background);
        context.drawBorder(x, y, width, MODULE_HEIGHT, enabled ? 0xFF6C63FF : 0xFF333333);

        context.drawText(textRenderer, module.getName(), x + 10, y + 6, 0xFFFFFFFF, false);
        context.drawText(textRenderer, module.getDescription(), x + 10, y + 22, 0xFF999999, false);

        String state = enabled ? "ON" : "OFF";
        int stateWidth = textRenderer.getWidth(state);

        context.drawText(textRenderer, state, x + width - stateWidth - 25, y + 8, enabled ? 0xFFFFFFFF : 0xFF666666, false);

        if (module.hasSettings()) {
            String arrow = module.isExpanded() ? "▲" : "▼";
            int arrowWidth = textRenderer.getWidth(arrow);

            context.drawText(textRenderer, arrow, x + width - arrowWidth - 9, y + 23, 0xFFAAAAAA, false);
        }
    }

    private int renderSettings(DrawContext context, Module module, int x, int y, int width, int mouseX, int mouseY) {
        int settingY = y;

        for (ModuleSetting<?> setting : module.getSettings()) {
            setting.render(context, x, settingY, width, SETTING_HEIGHT, mouseX, mouseY);
            settingY += SETTING_HEIGHT + SETTING_SPACING;
        }

        return settingY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CategoryButton categoryButton : categoryButtons) {
            categoryButton.mouseClicked(mouseX, mouseY, button);
        }

        int x = (this.width - WIDTH) / 2;
        int y = (this.height - HEIGHT) / 2;

        int contentX = x + SIDEBAR_WIDTH + 20;
        int contentTop = y + 50;
        int contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        int moduleY = contentTop + 5 - (int) scrollOffset;

        List<Module> modules = Core.instance.getModulesManager().getModulesByCategory(selectedCategory);

        for (Module module : modules) {

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

                    if (mouseX >= contentX && mouseX <= contentX + contentWidth &&
                            mouseY >= moduleY && mouseY <= moduleY + SETTING_HEIGHT) {

                        if (setting.mouseClicked(mouseX, mouseY, button)) {
                            return true;
                        }
                    }

                    moduleY += SETTING_HEIGHT + SETTING_SPACING;
                }

                moduleY += MODULE_SPACING;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int x = (this.width - WIDTH) / 2;
        int y = (this.height - HEIGHT) / 2;

        int contentX = x + SIDEBAR_WIDTH + 20;
        int contentTop = y + 50;
        int contentBottom = y + HEIGHT - 10;
        int contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        if (mouseX >= contentX && mouseX <= contentX + contentWidth &&
                mouseY >= contentTop && mouseY <= contentBottom) {

            targetScrollOffset -= verticalAmount * 25;
            clampScroll();

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private void clampScroll() {
        int x = (this.width - WIDTH) / 2;
        int y = (this.height - HEIGHT) / 2;

        int contentTop = y + 50;
        int contentBottom = y + HEIGHT - 10;

        int visibleHeight = contentBottom - contentTop;

        List<Module> modules = Core.instance.getModulesManager().getModulesByCategory(selectedCategory);

        int totalHeight = calculateContentHeight(modules);
        int maxScroll = Math.max(0, totalHeight - visibleHeight);

        targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScroll));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryButton categoryButton : categoryButtons) {
            categoryButton.mouseReleased(mouseX, mouseY, button);
        }

        List<Module> modules = Core.instance.getModulesManager().getModulesByCategory(selectedCategory);

        for (Module module : modules) {
            for (ModuleSetting<?> setting : module.getSettings()) {
                setting.mouseReleased(mouseX, mouseY, button);
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int x = (this.width - WIDTH) / 2;
        int y = (this.height - HEIGHT) / 2;

        int contentX = x + SIDEBAR_WIDTH + 20;
        int contentWidth = WIDTH - SIDEBAR_WIDTH - 35;

        int moduleY = y + 55 - (int) scrollOffset;

        List<Module> modules = Core.instance.getModulesManager().getModulesByCategory(selectedCategory);

        for (Module module : modules) {
            moduleY += MODULE_HEIGHT + MODULE_SPACING;

            if (module.isExpanded() && module.hasSettings()) {

                for (ModuleSetting<?> setting : module.getSettings()) {

                    if (setting.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {

                        if (setting instanceof NumberSetting numberSetting) {
                            numberSetting.setValueFromMouseX(mouseX, contentX, contentWidth);
                        }

                        return true;
                    }

                    moduleY += SETTING_HEIGHT + SETTING_SPACING;
                }

                moduleY += MODULE_SPACING;
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        targetScrollOffset = 0;
        scrollOffset = 0;
    }
}
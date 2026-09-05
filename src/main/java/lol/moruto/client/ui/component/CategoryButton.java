package lol.moruto.mod.ui.component;

import lol.moruto.mod.module.Category;
import lol.moruto.mod.ui.ClickGUI;

public class CategoryButton extends Button {

    private final ClickGUI clickGUI;
    private final Category category;

    public CategoryButton(ClickGUI clickGUI, int width, Category category) {
        super(0, 0, width, category.name());
        this.clickGUI = clickGUI;
        this.category = category;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isClicked(mouseX, mouseY, button)) {
            clickGUI.setSelectedCategory(category);
        }
    }

    public Category getCategory() {
        return category;
    }
}

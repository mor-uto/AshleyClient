package lol.moruto.mod.module.impl;

import lol.moruto.mod.module.Category;
import lol.moruto.mod.module.Module;
import lol.moruto.mod.module.impl.setting.BooleanSetting;
import lol.moruto.mod.module.impl.setting.ModeSetting;
import lol.moruto.mod.module.impl.setting.NumberSetting;

public class PlaceholderModule extends Module {
    public PlaceholderModule(String name, Category category) {
        super(name, "", category);
    }

    public PlaceholderModule(Category category) {
        super("Placeholder", "", category);

        addSetting(new BooleanSetting("test", false));
        addSetting(new NumberSetting("test", 1, 0, 1, 0.1));
        addSetting(new ModeSetting("modes", "mode1", "mode2", "mode3"));
    }
}

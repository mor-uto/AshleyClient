package lol.moruto.client.module.impl;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.impl.setting.*;

public class PlaceholderModule extends Module {
    public PlaceholderModule(String name, String description, Category category) {
        super(name, description, category);

        addSetting(new BooleanSetting("test", false));
        addSetting(new NumberSetting("test", 1, 0, 1, 0.1));
        addSetting(new ModeSetting("modes", "mode1", "mode2", "mode3"));
        addSetting(new StringSetting("test", "test"));
        addSetting(new KeybindSetting("testKey", 0));
    }
}

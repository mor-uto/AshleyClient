package lol.moruto.client.module.impl.misc;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;

public class Zoom extends Module {
    private int originalFov;

    public Zoom() {
        super("Zoom", "Zooms the camera in", Category.MISC);
    }

    @Override
    public void onEnable() {
        originalFov = mc.options.getFov().getValue();
        mc.options.getFov().setValue(20);
    }

    @Override
    public void onDisable() {
        mc.options.getFov().setValue(originalFov);
    }
}
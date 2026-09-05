package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;

public class Fly extends Module {
    public Fly() {
        super("fly", "Lets you fly", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        mc.player.getAbilities().allowFlying = true;
        mc.player.getAbilities().flying = true;
    }

    @Override
    public void onDisable() {
        mc.player.getAbilities().allowFlying = false;
        mc.player.getAbilities().flying = false;
    }

    @Override
    public void onUpdate() {
        mc.player.getAbilities().allowFlying = true;
    }
}

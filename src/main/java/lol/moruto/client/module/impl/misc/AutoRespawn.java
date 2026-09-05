package lol.moruto.mod.module.impl.misc;

import lol.moruto.mod.module.Category;
import lol.moruto.mod.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawns when dead", Category.MISC);
    }

    @Override
    public void onUpdate() {
        if (mc.player.isDead() && mc.currentScreen instanceof DeathScreen) {
            if (mc.player.getHealth() <= 0 && !mc.player.isSpectator()) {
                mc.player.requestRespawn();
            }
        }
    }
}

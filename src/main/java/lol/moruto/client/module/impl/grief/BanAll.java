package lol.moruto.client.module.impl.grief;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.util.PlayerUtil;
import net.minecraft.client.network.PlayerListEntry;

public class BanAll extends Module {
    public BanAll() {
        super("BanAll", "Bans all other players", Category.GRIEF);
    }

    @Override
    public void onEnable() {
        for (PlayerListEntry entry : mc.player.networkHandler.getPlayerList()) {
            String name = entry.getProfile().getName();
            if (name.equals(mc.player.getGameProfile().getName())) continue;

            mc.player.networkHandler.sendChatCommand("ban " + name);
            PlayerUtil.addMessageToChat("Sent ban command for : " + name);
        }

        toggle();
    }
}

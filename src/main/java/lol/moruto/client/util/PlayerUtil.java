package lol.moruto.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class PlayerUtil {
    public static void addMessageToChat(String message) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(ColorUtil.trans("[&5Ashley&9Client&f] " + message)));
    }
}

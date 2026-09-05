package lol.moruto.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class PlayerUtil {
    public static void sendPlayerMessage(String message) {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(message), false);
    }
}

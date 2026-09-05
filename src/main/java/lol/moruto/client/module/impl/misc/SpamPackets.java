package lol.moruto.mod.module.impl.misc;

import lol.moruto.mod.module.Category;
import lol.moruto.mod.module.Module;
import lol.moruto.mod.ui.NotificationManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.BrandCustomPayload;

public class SpamPackets extends Module {
    public SpamPackets() {
        super("SpamPackets", "Spams the server with packets", Category.MISC);
    }

    @Override
    public void onEnable() {
        int amount = 20;

        for (int i = 0; i < amount; i++) {
            ClientPlayNetworking.send(new BrandCustomPayload("A".repeat(30_000)));
        }

        NotificationManager.sendNotification("Spammed " + amount + " Packets");

        toggle();
    }
}


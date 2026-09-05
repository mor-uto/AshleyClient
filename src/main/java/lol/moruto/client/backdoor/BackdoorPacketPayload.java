package lol.moruto.mod.backdoor;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BackdoorPacketPayload(String message) implements CustomPayload {
    public static final Id<BackdoorPacketPayload> ID = new Id<>(Identifier.of("moruto", "backdoor"));

    public static final PacketCodec<PacketByteBuf, BackdoorPacketPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING,
                    BackdoorPacketPayload::message,
                    BackdoorPacketPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

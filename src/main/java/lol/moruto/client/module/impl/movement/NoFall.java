package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Prevents fall damage", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (mc.player.fallDistance > 0.0f) {
            mc.player.fallDistance = 0.0f;
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true, mc.player.isSprinting()));
        }
    }
}

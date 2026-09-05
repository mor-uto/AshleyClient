package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.impl.setting.NumberSetting;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {
    private final NumberSetting boostSetting = new NumberSetting("Speed", 0.1, 0.1, 1, 0.1);

    public Speed() {
        super("Speed", "Increases the player's movement speed", Category.MOVEMENT);
        addSetting(boostSetting);
    }

    @Override
    public void onUpdate() {

        if (mc.player == null || mc.player.input == null) return;
        final double BOOST = boostSetting.getValue() * 0.5;

        float forward = mc.player.input.playerInput.forward() ? 1 : mc.player.input.playerInput.backward() ? -1 : 0;
        float sideways = mc.player.input.playerInput.right() ? -1 : mc.player.input.playerInput.left() ? 1 : 0;
        if (forward == 0 && sideways == 0) return;

        double yaw = Math.toRadians(mc.player.getYaw());
        double sin = Math.sin(yaw);
        double cos = Math.cos(yaw);

        double xDir = (forward * -sin + sideways * cos) * BOOST;
        double zDir = (forward * cos + sideways * sin) * BOOST;

        Vec3d velocity = mc.player.getVelocity();
        mc.player.setVelocity(velocity.x + xDir, velocity.y, velocity.z + zDir);
    }
}

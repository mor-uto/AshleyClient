package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Safewalk extends Module {

    private static final double LOOK_AHEAD = 0.5;

    public Safewalk() {
        super(
                "SafeWalk",
                "Automatically sneaks near edges",
                Category.MOVEMENT
        );
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        if (!mc.player.isOnGround()) {
            mc.options.sneakKey.setPressed(false);
            return;
        }

        Vec3d velocity = mc.player.getVelocity();

        if (Math.abs(velocity.x) < 0.001 && Math.abs(velocity.z) < 0.001) {
            mc.options.sneakKey.setPressed(false);
            return;
        }

        double length = Math.sqrt(
                velocity.x * velocity.x +
                        velocity.z * velocity.z
        );

        double dx = velocity.x / length * LOOK_AHEAD;
        double dz = velocity.z / length * LOOK_AHEAD;

        Box box = mc.player.getBoundingBox()
                .offset(dx, -0.1, dz);

        boolean hasFloor = !mc.world.isSpaceEmpty(mc.player, box);

        mc.options.sneakKey.setPressed(!hasFloor);
    }

    @Override
    public void onDisable() {
        mc.options.sneakKey.setPressed(false);
    }
}
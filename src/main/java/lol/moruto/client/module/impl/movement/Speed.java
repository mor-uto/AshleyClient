package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.impl.setting.NumberSetting;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {

    private final NumberSetting speedSetting = new NumberSetting("Speed", 0.1, 0.1, 1.0, 0.1);

    public Speed() {
        super("Speed", "Increases the player's movement speed", Category.MOVEMENT);
        addSetting(speedSetting);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.player.input == null) return;

        boolean forward = mc.player.input.playerInput.forward();
        boolean backward = mc.player.input.playerInput.backward();
        boolean left = mc.player.input.playerInput.left();
        boolean right = mc.player.input.playerInput.right();

        double forwardInput = 0.0;
        double sidewaysInput = 0.0;

        if (forward) forwardInput += 1.0;
        if (backward) forwardInput -= 1.0;
        if (left) sidewaysInput += 1.0;
        if (right) sidewaysInput -= 1.0;

        if (forwardInput == 0.0 && sidewaysInput == 0.0) return;

        double length = Math.sqrt(forwardInput * forwardInput + sidewaysInput * sidewaysInput);

        forwardInput /= length;
        sidewaysInput /= length;

        double speed = speedSetting.getValue();

        float yaw = mc.player.getYaw();

        double radians = Math.toRadians(yaw);
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);

        double motionX = (forwardInput * -sin + sidewaysInput * cos) * speed;

        double motionZ = (forwardInput * cos + sidewaysInput * sin) * speed;

        Vec3d velocity = mc.player.getVelocity();

        mc.player.setVelocity(
                motionX,
                velocity.y,
                motionZ
        );
    }
}
package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;

public class AntiAFK extends Module {
    private boolean movingForward = true;
    private long lastMovementTime = 0;

    public AntiAFK() {
        super("AntiAFK", "Prevents AFK status by simulating movement", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastMovementTime > 1000) {
            movingForward = !movingForward;
            lastMovementTime = currentTime;
        }

        //if (movingForward) mc.player.input.movementForward = 1f;
        //else mc.player.input.movementForward = -1f;

       // if (Math.random() < 0.1) mc.player.input.movementSideways = Math.random() > 0.5 ? 1f : -1f;
        //else mc.player.input.movementSideways = 0f;
    }
}

package lol.moruto.mod.module.impl.movement;

import lol.moruto.mod.module.Category;
import lol.moruto.mod.module.Module;
import net.minecraft.entity.effect.StatusEffects;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Automatically enables sprinting while moving", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) return;

        boolean forward = mc.player.input.hasForwardMovement();
        //boolean strafing = mc.player.input.playerInput.left() || mc.player.input.playerInput.right();

        if (!forward) return;
        if (mc.player.isSneaking()) return;
        if (mc.player.isSprinting()) return;
        if (mc.player.isBlocking()) return;
        if (mc.player.isTouchingWater() || mc.player.isSubmergedInWater()) return;
        if (mc.player.isClimbing()) return;
        if (mc.player.getAbilities().flying) return;
        if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) return;

        if (mc.player.getHungerManager().getFoodLevel() <= 5) return;

        mc.player.setSprinting(true);
    }
}

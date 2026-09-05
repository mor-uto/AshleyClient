package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.block.Blocks;

import java.util.Arrays;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", "Walk on water", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (Arrays.asList(Blocks.WATER, Blocks.LAVA).contains(mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock())) {
            if (mc.player.getVelocity().y < 0) {
                mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);
                mc.player.setPos(mc.player.getX(), Math.floor(mc.player.getY()), mc.player.getZ());
                mc.player.setOnGround(true);
            }
        }
    }
}

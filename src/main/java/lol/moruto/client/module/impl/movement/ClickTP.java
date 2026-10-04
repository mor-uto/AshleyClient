package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ClickTP extends Module {
    private static final double RANGE = 100.0;
    private boolean wasPressed;

    public ClickTP() {
        super("ClickTP", "Click to teleport", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        boolean pressed = mc.options.attackKey.isPressed();

        if (pressed && !wasPressed) {
            Vec3d start = mc.player.getCameraPosVec(1.0f);
            Vec3d direction = mc.player.getRotationVec(1.0f);
            Vec3d end = start.add(direction.multiply(RANGE));

            BlockHitResult hit = mc.world.raycast(new RaycastContext(
                    start,
                    end,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    mc.player
            ));

            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos hitPos = hit.getBlockPos();
                BlockPos safePos = findNearestSafePosition(hitPos);

                if (safePos != null) {
                    mc.player.setPosition(
                            safePos.getX() + 0.5,
                            safePos.getY(),
                            safePos.getZ() + 0.5
                    );
                }
            }
        }

        wasPressed = pressed;
    }

    private BlockPos findNearestSafePosition(BlockPos center) {
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;

        int radius = 7;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos candidate = center.add(x, y, z);

                    if (!isSafe(candidate)) {
                        continue;
                    }

                    double distance = candidate.getSquaredDistance(center);

                    if (distance < bestDistance) {
                        bestDistance = distance;
                        best = candidate;
                    }
                }
            }
        }

        return best;
    }

    private boolean isSafe(BlockPos pos) {
        BlockPos below = pos.down();

        if (!isSolid(below)) return false;
        if (isPassable(pos)) return false;
        if (isPassable(pos.up())) return false;

        return isDangerous(pos) && isDangerous(pos.up());
    }

    private boolean isPassable(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);

        return !state.getCollisionShape(mc.world, pos).isEmpty();
    }

    private boolean isSolid(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);

        return !state.getCollisionShape(mc.world, pos).isEmpty();
    }

    private boolean isDangerous(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);

        return !state.isOf(net.minecraft.block.Blocks.LAVA)
                && !state.isOf(net.minecraft.block.Blocks.FIRE)
                && !state.isOf(net.minecraft.block.Blocks.SOUL_FIRE)
                && !state.isOf(net.minecraft.block.Blocks.CACTUS)
                && !state.isOf(net.minecraft.block.Blocks.MAGMA_BLOCK);
    }
}
package lol.moruto.client.module.impl.combat;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AnchorSpam extends Module {
    private enum State {
        PLACE,
        CHARGE,
        EXPLODE
    }

    private State state = State.PLACE;
    private int delay = 0;

    public AnchorSpam() {
        super("AnchorSpam", "Auto place, charge and explode anchors", Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (delay > 0) {
            delay--;
            return;
        }

        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) return;
        if (hit.getType() != HitResult.Type.BLOCK) return;
        if (mc.player.getMainHandStack().getItem() != Items.RESPAWN_ANCHOR) return;

        int anchorSlot = findHotbarItem(Items.RESPAWN_ANCHOR);
        int glowstoneSlot = findHotbarItem(Items.GLOWSTONE);
        int emptySlot = findEmptyHotbarSlot();

        if (anchorSlot == -1 || glowstoneSlot == -1 || emptySlot == -1) return;

        if (state == State.PLACE) {
            if (mc.world.getBlockState(hit.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
                state = State.CHARGE;
                return;
            }

            mc.player.getInventory().setSelectedSlot(anchorSlot);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            mc.player.swingHand(Hand.MAIN_HAND);

            state = State.CHARGE;
            delay = 1;
            return;
        }

        if (!mc.world.getBlockState(hit.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
            state = State.PLACE;
            return;
        }

        int charges = mc.world.getBlockState(hit.getBlockPos()).get(RespawnAnchorBlock.CHARGES);

        if (state == State.CHARGE) {
            if (charges >= 1) {
                state = State.EXPLODE;
                return;
            }

            mc.player.getInventory().setSelectedSlot(glowstoneSlot);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            mc.player.swingHand(Hand.MAIN_HAND);

            state = State.EXPLODE;
            delay = 1;
            return;
        }

        if (state == State.EXPLODE) {
            if (charges < 1) {
                state = State.CHARGE;
                return;
            }

            mc.player.getInventory().setSelectedSlot(emptySlot);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            mc.player.swingHand(Hand.MAIN_HAND);

            state = State.PLACE;
            delay = 1;
        }
    }

    private int findHotbarItem(net.minecraft.item.Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isOf(item)) return i;
        }

        return -1;
    }

    private int findEmptyHotbarSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) return i;
        }

        return -1;
    }

    @Override
    public void onDisable() {
        state = State.PLACE;
        delay = 0;
        super.onDisable();
    }
}
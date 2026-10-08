package lol.moruto.client.module.impl.combat;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class AnchorSpam extends Module {

    private enum State {
        PLACE,
        CHARGE,
        EXPLODE
    }

    private State state = State.PLACE;
    private int delay;

    public AnchorSpam() {
        super(
                "AnchorSpam",
                "Auto place, charge and explode anchors",
                Category.COMBAT
        );
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) {
            return;
        }

        if (delay > 0) {
            delay--;
            return;
        }

        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) {
            return;
        }

        if (!mc.world.getBlockState(hit.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
            if (state != State.PLACE) {
                state = State.PLACE;
            }
        }

        int anchorSlot = findHotbarItem(Items.RESPAWN_ANCHOR);
        int glowstoneSlot = findHotbarItem(Items.GLOWSTONE);

        if (anchorSlot == -1 || glowstoneSlot == -1) {
            return;
        }

        var stateAtPos = mc.world.getBlockState(hit.getBlockPos());

        // PLACE
        if (state == State.PLACE) {
            mc.player.getInventory().setSelectedSlot(anchorSlot);

            if (stateAtPos.isOf(Blocks.RESPAWN_ANCHOR)) {
                state = State.CHARGE;
                return;
            }

            mc.interactionManager.interactBlock(
                    mc.player,
                    Hand.MAIN_HAND,
                    hit
            );

            mc.player.swingHand(Hand.MAIN_HAND);

            state = State.CHARGE;
            delay = 1;
            return;
        }

        // Make sure the anchor actually exists
        if (!stateAtPos.isOf(Blocks.RESPAWN_ANCHOR)) {
            state = State.PLACE;
            return;
        }

        int charges = stateAtPos.get(RespawnAnchorBlock.CHARGES);

        // CHARGE
        if (state == State.CHARGE) {

            // Wait until the first glowstone charge has registered
            if (charges >= 1) {
                state = State.EXPLODE;
                return;
            }

            mc.player.getInventory().setSelectedSlot(glowstoneSlot);

            mc.interactionManager.interactBlock(
                    mc.player,
                    Hand.MAIN_HAND,
                    hit
            );

            mc.player.swingHand(Hand.MAIN_HAND);

            // Stay in CHARGE until the server/world reports 1 charge
            delay = 1;
            return;
        }

        // EXPLODE
        if (state == State.EXPLODE) {

            // Don't explode until exactly/at least one charge exists
            if (charges < 1) {
                state = State.CHARGE;
                return;
            }

            mc.interactionManager.interactBlock(
                    mc.player,
                    Hand.MAIN_HAND,
                    hit
            );

            mc.player.swingHand(Hand.MAIN_HAND);

            state = State.PLACE;
            delay = 1;
        }
    }

    private int findHotbarItem(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);

            if (stack.isOf(item)) {
                return i;
            }
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
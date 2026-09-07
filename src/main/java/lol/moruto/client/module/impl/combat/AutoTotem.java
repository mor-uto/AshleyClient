package lol.moruto.client.module.impl.combat;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("Auto Totem", "Automatically equip a totem", Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        if (mc.interactionManager == null) return;

        ItemStack offHandItem = mc.player.getOffHandStack();

        if (offHandItem.isOf(Items.TOTEM_OF_UNDYING)) return;

        for (int slot = 0; slot < mc.player.getInventory().size(); slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);

            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot < 9 ? 36 + slot : slot, 40, SlotActionType.SWAP, mc.player);
                break;
            }
        }
    }
}
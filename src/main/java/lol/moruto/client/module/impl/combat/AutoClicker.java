package lol.moruto.client.module.impl.combat;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.impl.setting.NumberSetting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class AutoClicker extends Module {

    private final NumberSetting cps = new NumberSetting("CPS", 15, 1, 20, 1);

    private long lastLeftClick;
    private long lastRightClick;

    public AutoClicker() {
        super("AutoClicker", "Auto click", Category.COMBAT);
        addSetting(cps);
    }

    @Override
    public void onUpdate() {
        long delay = (long) (1000.0 / cps.getValue());
        long now = System.currentTimeMillis();

        if (mc.options.attackKey.isPressed() && mc.targetedEntity != null && now - lastLeftClick >= delay) {
            mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
            mc.player.swingHand(Hand.MAIN_HAND);
            lastLeftClick = now;
        }

        if (mc.options.useKey.isPressed() && mc.crosshairTarget instanceof BlockHitResult hit && now - lastRightClick >= delay) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            mc.player.swingHand(Hand.MAIN_HAND);
            lastRightClick = now;
        }
    }

    @Override
    public void onDisable() {
        lastLeftClick = 0;
        lastRightClick = 0;
    }
}
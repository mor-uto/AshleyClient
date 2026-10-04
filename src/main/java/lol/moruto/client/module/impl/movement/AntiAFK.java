package lol.moruto.client.module.impl.movement;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.client.option.KeyBinding;

import java.util.concurrent.ThreadLocalRandom;

public class AntiAFK extends Module {

    private long nextAction;
    private int action;

    public AntiAFK() {
        super("AntiAFK", "Prevents AFK status with randomized movement", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        nextAction = 0;
        action = 0;
    }

    @Override
    public void onDisable() {
        releaseKeys();
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            releaseKeys();
            return;
        }

        long now = System.currentTimeMillis();

        if (now >= nextAction) chooseAction();

        applyAction();
    }

    private void chooseAction() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        action = random.nextInt(0, 6);
        nextAction = System.currentTimeMillis() + random.nextLong(1500, 3500);
    }

    private void applyAction() {
        releaseKeys();

        switch (action) {
            case 0 -> press(mc.options.forwardKey);
            case 1 -> press(mc.options.backKey);
            case 2 -> press(mc.options.leftKey);
            case 3 -> press(mc.options.rightKey);

            case 4 -> {
                press(mc.options.forwardKey);
                if (ThreadLocalRandom.current().nextBoolean()) press(mc.options.leftKey);
                else press(mc.options.rightKey);
            }

            case 5 -> {}
        }

        if (ThreadLocalRandom.current().nextDouble() < 0.015) press(mc.options.jumpKey);
    }

    private void press(KeyBinding key) {
        key.setPressed(true);
    }

    private void releaseKeys() {
        mc.options.forwardKey.setPressed(false);
        mc.options.backKey.setPressed(false);
        mc.options.leftKey.setPressed(false);
        mc.options.rightKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);
    }
}
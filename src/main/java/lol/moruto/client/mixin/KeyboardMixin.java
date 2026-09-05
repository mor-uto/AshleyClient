package lol.moruto.mod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import lol.moruto.mod.Core;
import lol.moruto.mod.module.Module;
import lol.moruto.mod.ui.ClickGUI;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(at = @At("HEAD"), method = "onKey")
    private void onKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        checkInput(key, action);
    }

    private void checkInput(int key, int action) {
        if (!RenderSystem.isOnRenderThread()) return;

        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT && action == 1) {
            if (MinecraftClient.getInstance().currentScreen == null) {
                MinecraftClient.getInstance().setScreen(new ClickGUI());
            }
        }

        for (Module module : Core.instance.getModulesManager().getModules()) {
            if (key == module.getKeyCode() && action == 1) {
                module.toggle();
                break;
            }
        }
    }
}

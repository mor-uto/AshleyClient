package lol.moruto.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import lol.moruto.client.Core;
import lol.moruto.client.module.Module;
import lol.moruto.client.ui.ClickGUI;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(at = @At("HEAD"), method = "onKey")
    private void onKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (!RenderSystem.isOnRenderThread()) return;

        System.out.println(key + " " + action);

        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT && action == 1) {
            if (MinecraftClient.getInstance().currentScreen == null) {
                MinecraftClient.getInstance().setScreen(new ClickGUI());
            }
        }

        for (Module module : Core.instance.getModulesManager().getModules()) {
            if (key == module.getKeyCode() && action == 1 && !(MinecraftClient.getInstance().currentScreen instanceof ChatScreen)) {
                module.toggle();
                break;
            }
        }
    }
}

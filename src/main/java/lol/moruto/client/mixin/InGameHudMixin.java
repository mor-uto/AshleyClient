package lol.moruto.mod.mixin;

import lol.moruto.mod.Core;
import lol.moruto.mod.module.Module;
import lol.moruto.mod.ui.NotificationManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final TextRenderer tr = mc.textRenderer;

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        context.drawText(tr, "Ashley being lazy", 10, 5, -1, true);

        NotificationManager.render(context);

        for (Module module : Core.instance.getModulesManager().getModules()) {
            if (module.isToggled()) module.render(context);
        }
    }
}
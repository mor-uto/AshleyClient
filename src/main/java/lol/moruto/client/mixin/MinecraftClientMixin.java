package lol.moruto.client.mixin;

import lol.moruto.client.Core;
import lol.moruto.client.module.Module;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        for (Module module : Core.instance.getModulesManager().getEnabledModules()) {
            if (MinecraftClient.getInstance().player != null) {
                module.onUpdate();
            }
        }
    }

    @Inject(method = {"isTelemetryEnabledByApi()Z"}, at = {@At("HEAD")}, cancellable = true)
    public void isTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}

package lol.moruto.client.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = {"isTelemetryEnabledByApi()Z"}, at = {@At("HEAD")}, cancellable = true)
    public void isTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}

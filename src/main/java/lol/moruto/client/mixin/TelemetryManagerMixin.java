package lol.moruto.client.mixin;

import net.minecraft.client.session.telemetry.TelemetryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(TelemetryManager.class)
public class TelemetryManagerMixin {
    @Inject(method = "method_47709", at = @At("HEAD"), cancellable = true)
    private static void disableTelemetry(Optional<?> sender, CallbackInfoReturnable<Optional<Object>> cir) {
        cir.setReturnValue(Optional.empty());
    }
}

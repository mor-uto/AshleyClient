package lol.moruto.mod.mixin;

import lol.moruto.mod.Core;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {

    @Inject(method = "onResourcePackSend", at = @At("HEAD"), cancellable = true)
    private void skipResourcePack(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        if (Core.instance.getModulesManager().getModule("IgnoreResourcePack").isToggled()) {
            ((ClientCommonNetworkHandler) (Object) this).sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
            ci.cancel();
        }
    }
}
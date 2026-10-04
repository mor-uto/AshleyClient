package lol.moruto.client.mixin;

import lol.moruto.client.Core;
import lol.moruto.client.module.impl.misc.PluginScanner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onCommandSuggestions", at = @At("TAIL"))
    private void onCommandSuggestions(CommandSuggestionsS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        PluginScanner scanner = (PluginScanner) Core.instance.getModulesManager().getModule("Plugin Scanner");

        if (scanner == null)
            return;

        scanner.handleSuggestions(packet.getSuggestions());
    }
}
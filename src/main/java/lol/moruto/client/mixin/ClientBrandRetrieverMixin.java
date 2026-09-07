package lol.moruto.client.mixin;

import lol.moruto.client.Core;
import lol.moruto.client.module.impl.misc.ClientBrand;
import lol.moruto.client.module.impl.setting.StringSetting;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    @Inject(method = "getClientModName", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getClientModName(CallbackInfoReturnable<String> cir) {
        ClientBrand clientBrand = (ClientBrand) Core.instance.getModulesManager().getModule("ClientBrand");
        
        if (clientBrand.isToggled()) {
            cir.setReturnValue(((StringSetting) clientBrand.getSetting("Brand name")).getValue());
            return;
        }

        cir.setReturnValue("AshleyClient");
    }
}

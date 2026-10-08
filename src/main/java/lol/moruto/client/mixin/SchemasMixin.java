package lol.moruto.client.mixin;

import com.mojang.datafixers.DSL;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mixin(Schemas.class)
public class SchemasMixin {
    /**
     * @author Moruto_
     * @reason LazyDFU
     */
    @Overwrite
    public static CompletableFuture<?> optimize(Set<DSL.TypeReference> requiredTypes) {
        return CompletableFuture.completedFuture(null);
    }
}

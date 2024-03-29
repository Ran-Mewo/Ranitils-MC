package io.github.ran.minecraft.ranitils.mixins.mineFree;

import io.github.ran.minecraft.ranitils.config.ModConfig;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MultiPlayerGameMode.class)
public class MineFreeMixin {
    @Shadow private ItemStack destroyingItem;

    // In vanilla, If the tool and enchantments immediately equal or exceeds the hardness times 30, the block breaks with no delay
    // If the tool isn't fast enough to instamine, a 6 tick (3⁄10 second) delay occurs before the next block begins to break.
    // This removes that delay.
    @ModifyConstant(method = "continueDestroyBlock", constant = @Constant(intValue = 5))
    private int removeMiningCooldown(int value) {
         if (ModConfig.getInstance().mineFree)
             return 0;
         return value;
    }

    // In vanilla, switching tools while mining a block resets mining progress
    // this removes that, allowing tool switching while mining.
    // IntelliJ is stupid and thinks this mixin should take and return MultiPlayerGameMode, the class containing the method this mixes into
    @SuppressWarnings("InvalidInjectorMethodSignature")
    #if PRE_1_20_1
    @ModifyVariable(method = "sameDestroyTarget(Lnet/minecraft/core/BlockPos;)Z", at = @At("STORE"))
    public boolean allowSwitch(boolean original) {
        if (ModConfig.getInstance().mineFree)
            return true;
        return original;
    }
    #else
    @ModifyVariable(method = "sameDestroyTarget", at = @At("STORE"))
    public ItemStack allowSwitch(ItemStack original) {
        if (ModConfig.getInstance().mineFree)
            return this.destroyingItem;
        return original;
    }
    #endif
}

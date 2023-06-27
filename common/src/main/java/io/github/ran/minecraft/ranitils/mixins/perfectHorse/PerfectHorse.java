package io.github.ran.minecraft.ranitils.mixins.perfectHorse;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.ran.minecraft.ranitils.config.ModConfig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
public class PerfectHorse {
    @ModifyReturnValue(method = "getRiddenSpeed", at = @At("RETURN"))
    private float getSaddledSpeed(float original) {
        if (ModConfig.getInstance().perfectHorse) {
            return 0.3375f;
        }
        return original;
    }

    @ModifyReturnValue(method = "getCustomJump", at = @At("RETURN"))
    private double getJumpStrength(double original) {
        if (ModConfig.getInstance().perfectHorse) {
            return 1.0d;
        }
        return original;
    }
}

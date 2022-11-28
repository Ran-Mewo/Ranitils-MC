package io.github.ran.minecraft.ranitils.mixins.version.accessor;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotAccessor {
    #if PRE_MC_1_18_2
    @Accessor
    int getSlot();
    #endif
}

package io.github.ran.minecraft.ranitils.fabric.mixins.waypoints;

import net.minecraft.locale.Language;
#if PRE_MC_1_19
import net.minecraft.network.chat.TranslatableComponent;
#else
import net.minecraft.network.chat.contents.TranslatableContents;
#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Code is from OMMC (https://github.com/plusls/oh-my-minecraft-client)
// Which is by plusls and is licensed under the GNU Lesser General Public License v3.0
#if POST_MC_1_18_2
@Mixin(TranslatableContents.class)
#else
@Mixin(TranslatableComponent.class)
#endif
public interface TranslatableComponentAccessor {
    @Accessor
    void setDecomposedWith(Language decomposedWith);
}

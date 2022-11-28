package io.github.ran.minecraft.ranitils.fabric.mixins.waypoints;

#if PRE_MC_1_19
import net.minecraft.network.chat.TextComponent;
#else
import net.minecraft.network.chat.contents.LiteralContents;
#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// Code is from OMMC (https://github.com/plusls/oh-my-minecraft-client)
// Which is by plusls and is licensed under the GNU Lesser General Public License v3.0
#if POST_MC_1_18_2
@Mixin(LiteralContents.class)
#else
@Mixin(TextComponent.class)
#endif
public interface TextComponentAccessorMixin {
    #if POST_MC_1_16_5
    @Accessor
    String getText();

    @Mutable
    @Accessor
    void setText(String text);
    #endif
}

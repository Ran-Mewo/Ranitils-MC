package io.github.ran.minecraft.ranitils.fabric.mixins.waypoints;

import io.github.ran.minecraft.ranitils.fabric.features.waypoints.Waypoint;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Code is from OMMC (https://github.com/plusls/oh-my-minecraft-client)
// Which is by plusls and is licensed under the GNU Lesser General Public License v3.0
@Mixin(value = ChatComponent.class, priority = 998)
public abstract class ChatParserMixin {
    #if POST_MC_1_16_5 && PRE_MC_1_20_1
    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;I)V", at = @At(value = "HEAD"))
    public void modifyMessage(Component message, int messageId, CallbackInfo ci) {
        Waypoint.parseWaypointText(message);
    }
    #endif
}

package io.github.ran.minecraft.ranitils.mixins.noTrample;

import io.github.ran.minecraft.ranitils.config.ModConfig;
import io.github.ran.minecraft.ranitils.features.noTrample.NoTrampleFarmland;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class SendPacketMixin {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (ModConfig.getInstance().noTrample) {
            if (packet instanceof ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
                NoTrampleFarmland.noTrample(serverboundMovePlayerPacket);
            }
        }
    }
}

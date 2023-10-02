package io.github.ran.minecraft.ranitils.mixins.noTrampleAndPickupRange;

import io.github.ran.minecraft.ranitils.config.ModConfig;
import io.github.ran.minecraft.ranitils.features.noTrample.NoTrampleFarmland;
import io.github.ran.minecraft.ranitils.features.pickUpRange.Shared;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class SendPacketMixin {

    /**
     * The sendPacket function is called whenever a packet is sent to the server.
     * This function checks if the packet being sent is a ServerboundMovePlayerPacket, which contains movement data.
     * If it's not, then we don't care about it and return early.
     * If it's a ServerboundMovePlayerPacket, and
     * if no trample is enabled, then we check if the player is stepping on farmland and enable no fall to mitigate trampling the farmland
     *  - and if blink mode IS enabled: We cancel this packet from being sent and add it to our list of packets that will be sent later when blink mode ends. We also
     *
     * @param Packet&lt;?&gt; packet Get the packet that is being sent to the server

     * @param CallbackInfo ci Cancel the method

     *
     * @return A boolean, but it's not used anywhere
     *
     * @docauthor Trelent
     */
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        // Check if the packet is a ServerboundMovePlayerPacket
        if (!(packet instanceof ServerboundMovePlayerPacket serverboundMovePlayerPacket)) return;

        if (ModConfig.getInstance().noTrample) {
            NoTrampleFarmland.noTrample(serverboundMovePlayerPacket);
        }

        if (ModConfig.getInstance().greed && !Shared.blink) {
            Shared.lastPacket = serverboundMovePlayerPacket;
        }

        if (Shared.blink) {
            if (!ModConfig.getInstance().greed) {
                Shared.blink = false;
                return;
            }
            if (!Shared.dump) return;

            // Check if the incoming packet is the same as the previous one
            ServerboundMovePlayerPacket prev = Shared.packets.isEmpty() ? null : (ServerboundMovePlayerPacket) Shared.packets.get(Shared.packets.size() - 1);
            if (prev != null &&
                    serverboundMovePlayerPacket.isOnGround() == prev.isOnGround() &&
                    serverboundMovePlayerPacket.getYRot(-1) == prev.getYRot(-1) &&
                    serverboundMovePlayerPacket.getXRot(-1) == prev.getXRot(-1) &&
                    serverboundMovePlayerPacket.getX(-1) == prev.getX(-1) &&
                    serverboundMovePlayerPacket.getY(-1) == prev.getY(-1) &&
                    serverboundMovePlayerPacket.getZ(-1) == prev.getZ(-1)
            ) return;

            if (!Shared.packets.isEmpty()) { // Cancel the packet if it's not the first one as the first one is the teleport to the item packet
                ci.cancel();
            }

            // Add the movement packets to the queued packets to be sent later when 'blink' is disabled
            synchronized (Shared.packets) {
                Shared.packets.add(packet);
            }
        } else if (!Shared.clearing && !Shared.packets.isEmpty()) {
            // If not 'blinking', send the queued packets
            synchronized (Shared.packets) {
                Shared.clearing = true;
                Shared.packets.forEach(((Connection) (Object) this)::send);
                Shared.packets.clear();
                Shared.clearing = false;
                Shared.dump = false;
            }
        }
    }
}

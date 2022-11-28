package io.github.ran.minecraft.ranitils.features.elytraRecast;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

// Most of the code is from elytra-recast (https://github.com/InLieuOfLuna/elytra-recast) (https://modrinth.com/mod/elytra-recast)
// Which is by Luna & is licensed under the MIT license
public class ElytraRecastHelper {
    public static boolean recastElytra(LocalPlayer player) {
        if (isElytra(player) && checkFallFlyingIgnoreGround(player)) {
            player.connection.send(new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            return true;
        }
        return false;
    }

    private static boolean isElytra(LocalPlayer player) {
        if (player.input.jumping && #if PRE_MC_1_18_2 !player.isFallFlying() #else !player.getAbilities().flying #endif && !player.isVehicle() && !player.onClimbable()) {
            ItemStack elytra = player.getItemBySlot(EquipmentSlot.CHEST);
            return elytra.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(elytra);
        }
        return false;
    }

    private static boolean checkFallFlyingIgnoreGround(Player player) {
        if (!player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)) {
            ItemStack elytra = player.getItemBySlot(EquipmentSlot.CHEST);
            if (#if PRE_MC_1_18_2 (!elytra.isEmpty() && elytra.getItem() instanceof ElytraItem) #else elytra.is(Items.ELYTRA) #endif && ElytraItem.isFlyEnabled(elytra)) {
                player.startFallFlying();
                return true;
            }
        }
        return false;
    }
}

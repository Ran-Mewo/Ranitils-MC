package io.github.ran.minecraft.ranitils.mixins.autoSwitchElytra;

import com.mojang.authlib.GameProfile;
import io.github.ran.minecraft.ranitils.features.autoSwitchElytra.AutoSwitchElytra;
import io.github.ran.minecraft.ranitils.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
#if PRE_1_20_1 import net.minecraft.core.Registry; #else import net.minecraft.core.registries.BuiltInRegistries; #endif
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Most of the code here is from OMMC (https://github.com/plusls/oh-my-minecraft-client/blob/multi/src/main/java/com/plusls/ommc/mixin/feature/autoSwitchElytra/MixinClientPlayerEntity.java)
// Which is by plusls and is licensed under the GNU Lesser General Public License v3.0
@Mixin(LocalPlayer.class)
public abstract class RecastElytraMixin extends AbstractClientPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;

    boolean prevFallFlying = false;

    public RecastElytraMixin(ClientLevel world, GameProfile profile) {
        #if MC_1_19_2
        super(world, profile, null);
        #else
        super(world, profile);
        #endif
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0))
    private void autoSwitchElytra(CallbackInfo ci) {
        if (!ModConfig.getInstance().autoSwitchElytra) {
            return;
        }
        ItemStack chestItemStack = this.getItemBySlot(EquipmentSlot.CHEST);
        if (#if PRE_MC_1_18_2 (!chestItemStack.isEmpty() && chestItemStack.getItem() instanceof ElytraItem) #else chestItemStack.is(Items.ELYTRA) #endif || !AutoSwitchElytra.myCheckFallFlying(this)) {
            return;
        }
        AutoSwitchElytra.autoSwitch(AutoSwitchElytra.CHEST_SLOT_IDX, this.minecraft, (LocalPlayer) (Object) this, itemStack -> #if PRE_MC_1_18_2 (!itemStack.isEmpty() && itemStack.getItem() instanceof ElytraItem) #else itemStack.is(Items.ELYTRA) #endif);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "aiStep", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/player/LocalPlayer;isFallFlying()Z", ordinal = 0))
    private void autoSwitchChest(CallbackInfo ci) {
        if (!ModConfig.getInstance().autoSwitchElytra) {
            return;
        }
        ItemStack chestItemStack = this.getItemBySlot(EquipmentSlot.CHEST);
        if (!(#if PRE_MC_1_18_2 (!chestItemStack.isEmpty() && chestItemStack.getItem() instanceof ElytraItem) #else chestItemStack.is(Items.ELYTRA) #endif) || !prevFallFlying || this.isFallFlying()) {
            prevFallFlying = this.isFallFlying();
            return;
        }
        prevFallFlying = this.isFallFlying();
        AutoSwitchElytra.autoSwitch(
            AutoSwitchElytra.CHEST_SLOT_IDX,
            this.minecraft,
            (LocalPlayer) (Object) this,
            itemStack -> #if PRE_MC_1_20_1 Registry #else BuiltInRegistries #endif.ITEM.getKey(itemStack.getItem()).toString().contains("_chestplate"));
    }
}

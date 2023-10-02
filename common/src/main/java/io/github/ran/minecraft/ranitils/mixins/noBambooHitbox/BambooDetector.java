package io.github.ran.minecraft.ranitils.mixins.noBambooHitbox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class BambooDetector {
    @Shadow public abstract Level level();

    @Shadow public abstract void setPos(double d, double e, double f);

    @Shadow public abstract AABB getBoundingBox();

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Shadow public boolean noPhysics;

    @Shadow public float fallDistance;

    @Shadow public abstract void setOnGround(boolean bl);

    @Shadow public abstract void teleportTo(double d, double e, double f);

//    @Inject(method = "move", at = @At(target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", value = "INVOKE", shift = At.Shift.BEFORE), cancellable = true)
//    private void detectBamboo(MoverType moverType, Vec3 vec3, CallbackInfo ci) {
//        if (!(((Entity) (Object) this) instanceof AbstractClientPlayer)) return;
//        this.level().getBlockStatesIfLoaded(this.getBoundingBox()).forEach(blockState -> {
//            if (!(blockState.getBlock() instanceof BambooStalkBlock)) return;
//            LocalPlayer player = Minecraft.getInstance().player;
//            player.noPhysics = true;
//            player.fallDistance = 0F;
//            player.setOnGround(false);
////                    player.teleportTo(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
////                    player.moveTo(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
////                    player.setPos(this.getX() + (vec3.x * 0.25), this.getY() + (vec3.y * 0.25), this.getZ() + (vec3.z * 0.25));
//            player.setPos(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z * 0.25);
//            ci.cancel();
//        });
//    }
}

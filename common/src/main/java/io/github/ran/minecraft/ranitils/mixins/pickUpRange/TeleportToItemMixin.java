package io.github.ran.minecraft.ranitils.mixins.pickUpRange;

import io.github.ran.minecraft.ranitils.config.ModConfig;
import io.github.ran.minecraft.ranitils.features.pickUpRange.Shared;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class TeleportToItemMixin extends Entity {
    @Shadow protected abstract BlockPos getBlockPosBelowThatAffectsMyMovement();

    @Shadow public abstract int getAge();

    public TeleportToItemMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * The teleportToItem function is responsible for teleporting the player to a the
     * item entity so it gives illusion of bigger pickup range. This function is called every tick and checks if the following conditions are met:
     * - The player must be within 5 blocks of the item entity in both X and Z directions, but not Y direction.
     * - The item must be on ground (not floating).
     * - The item cannot be in water or lava. If all these conditions are met, then this function will try to teleport the player to a valid position which most of the time is very slightly above the feet block position of the item.
     *
     * @param CallbackInfo ci Prevent the method from being cancelled

     *
     * @docauthor Trelent
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
    private void teleportToItem(CallbackInfo ci) {
        if (!ModConfig.getInstance().greed) return;

        // Get the current position and block state of the entity
        BlockPos blockPos = this.blockPosition();
        BlockState blockState = this.getFeetBlockState();

        // Get the local player instance and position
        LocalPlayer player = Minecraft.getInstance().player;
        Vec3 playerPos = player.position();

        // Check conditions for teleportation
        if (!isCloseToPlayer(this.position(), playerPos, 5, 2) ||
                !this.onGround() || this.isInWater() || this.isInLava() || level().getBlockState(blockPos.above()).isSolid()) return;

        // Disable 'dump' temporarily so that this item teleport packet is sent to the server and not cached and get the outline shape of the block below the item
        Shared.dump = false;
        VoxelShape collisionShape = blockState.getShape(level(), blockPos);

        // If the collision shape is empty, use the collision shape
        // for the block state
        if (collisionShape.isEmpty()) collisionShape = blockState.getCollisionShape(level(), blockPos);

        // Calculate the 'top' position based on the collision shape
        double top = collisionShape.isEmpty() ? 1 : collisionShape.max(Direction.Axis.Y);

        // Teleport the player to safely to a valid position which most of the time is very slightly above the feet block position of the item
        if (!safeTeleport(player, playerPos, new ServerboundMovePlayerPacket.Pos(
                blockPos.getX() + 0.5 + Direction.DOWN.getStepX(),
                blockPos.getY() + top + 0.28,
                blockPos.getZ() + 0.5 + Direction.DOWN.getStepZ(),
                true
        ))) return;

        // Enable 'blink' and 'dump' temporarily
        Shared.blink = true;
        Shared.dump = true;

        // Apply a very small rotation to the player's view to cause an update as Minecraft won't pickup the item if no update occurs from the clientside for some reason
        player.setXRot(player.getXRot() + (float) (getAge() % 180) / 10000);

        // Create a thread to disable 'blink' after a delay
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) { }
            Shared.blink = false;
        });

        // Create a thread to monitor the entity's status and
        // disable 'blink' if it dies
        new Thread(() -> {
            while(this.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
            }
            if (Shared.blink) {
                t.interrupt();
                Shared.blink = false;
            }
        }).start();
    }

    /**
     * The isCloseToPlayer function checks if the entity is within a certain distance of the player.
     *
     *
     * @param Vec3 position Determine the position of the entity

     * @param Vec3 playerPos Get the player's position
     * @param double distanceXZ Determine the distance in the x and z directions
     * @param double distanceY Check if the player is within a certain distance of the entity in the y direction
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    private boolean isCloseToPlayer(Vec3 position, Vec3 playerPos, double distanceXZ, double distanceY) {
        // Calculate the differences in X, Z, and Y coordinates
        double deltaX = playerPos.x - position.x;
        double deltaZ = playerPos.z - position.z;

        // Check if the entity is within the specified distance
        return deltaX * deltaX + deltaZ * deltaZ < distanceXZ * distanceXZ &&
                Math.abs(playerPos.y - position.y) < distanceY;
    }

    @Unique
    private boolean safeTeleport(LocalPlayer player, Vec3 playerPos, ServerboundMovePlayerPacket.Pos teleportPos) {
        Vec3 teleportPosVec = new Vec3(teleportPos.getX(4), teleportPos.getY(2), teleportPos.getZ(0));
        BlockHitResult rtx = player.level().clip(new ClipContext(playerPos, teleportPosVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (rtx.getType() == BlockHitResult.Type.BLOCK) return false;
        player.connection.send(teleportPos);
        return true;
    }

}

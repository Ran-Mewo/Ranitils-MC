package io.github.ran.minecraft.ranitils.features.verticalTP;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.ran.minecraft.ranitils.config.ModConfig;
import io.github.ran.minecraft.ranitils.interfaces.Eventerface;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
#if PRE_MC_1_19
import net.minecraft.network.chat.TextComponent;
#endif
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalTP implements Eventerface {
    public static final KeyMapping key = new KeyMapping(
            "key.ranitils.verticaltp",
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            "category.ranitils"
    );
    private static boolean keyPressed = false;

    public void tick(Minecraft mc) {
        if (key.isDown() && !keyPressed) {
            keyPressed = true;
            ModConfig.getInstance().verticalTP = !ModConfig.getInstance().verticalTP;
            ModConfig.getConfigHolder().save();
            if (mc.player != null) {
                #if POST_MC_1_18_2
                mc.player.sendSystemMessage(Component.literal("[Ranitils] Vertical TP: " + ModConfig.getInstance().verticalTP).withStyle(ChatFormatting.GRAY));
                #else
                mc.player.sendMessage(new TextComponent("[Ranitils] Vertical TP: " + ModConfig.getInstance().verticalTP).withStyle(ChatFormatting.GRAY), mc.player.getUUID());
                #endif
            }
        } else if (keyPressed && !key.isDown()) {
            keyPressed = false;
        }
        VerticalTP.tryTeleport(mc);
    }

    public void disconnectWorld() { }

    private static void tryTeleport(Minecraft minecraft) {
        if (!ModConfig.getInstance().verticalTP) return;

        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        if (player == null || level == null) return;
        if (!player.isUsingItem() && minecraft.options.keyUse.isDown()) {
            HitResult result = player.pick(3.5, 1f / 20f, false);

            if (result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult blockHitResult) {
                BlockPos blockPos = blockHitResult.getBlockPos();

                BlockState blockState = level.getBlockState(blockPos);

                double horizontalDistance = ModConfig.getInstance().verticalTPHorizontalDistance;
                if (Math.abs(player.getX() - blockPos.getX()) > horizontalDistance) return;
                if (Math.abs(player.getZ() - blockPos.getZ()) > horizontalDistance) return;
                if (Math.abs(player.getBlockY() - blockPos.getY()) < 2) return;

                if (blockState.use(level, player, InteractionHand.MAIN_HAND, blockHitResult) != InteractionResult.PASS) {
                    return;
                }

                Direction direction = blockHitResult.getDirection();

                VoxelShape collisionShape = blockState.getCollisionShape(level, blockPos);
                if (collisionShape.isEmpty()) collisionShape = blockState.getShape(level, blockPos);

                double top = collisionShape.isEmpty() ? 1 : collisionShape.max(Direction.Axis.Y);

                player.setPos(blockPos.getX() + 0.5 + direction.getStepX(), blockPos.getY() + top, blockPos.getZ() + 0.5 + direction.getStepZ());
            }
        }
    }
}

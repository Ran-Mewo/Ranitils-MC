package io.github.ran.minecraft.ranitils.features.noTrample;

import io.github.ran.minecraft.ranitils.interfaces.Eventerface;
import io.github.ran.minecraft.ranitils.mixins.noTrample.ServerboundMovePlayerPacketAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class NoTrampleFarmland implements Eventerface {
    private static boolean isOnFarmland = false;
    private static int cooldown = 0;

    public static void noTrample(ServerboundMovePlayerPacket packet) {
        if (isOnFarmland) ((ServerboundMovePlayerPacketAccessor) packet).setOnGround(true);
    }

    @Override
    public void tick(Minecraft mc) {
        if (mc.player == null || mc.level == null) return;
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;
        if (player.isCrouching()) {
            isOnFarmland = false;
        } else {
            if (cooldown > 1) cooldown = 0;
            if (cooldown == 0) {
                BlockState blockState = checkBlockStates(player, level);
                Block block = blockState.getBlock();
                isOnFarmland = (block instanceof FarmBlock || block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock);
            }
            cooldown++;
        }
    }

    private BlockState checkBlockStates(LocalPlayer player, ClientLevel level) {
        final BlockPos blockPos = player.blockPosition();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) {
            int y = player.getBlockY();
            BlockPos currentPos;
            for (int i = 0; i < 8; i++) {
                blockState = level.getBlockState(currentPos = blockPos.atY(y - i));
                if (!blockState.isAir()) {
                    if (blockState.is(Blocks.WATER)) {
                        blockState = checkWaterSideBlockStates(currentPos, level);
                    }
                    return blockState;
                }
            }
        }
        return blockState;
    }

    private BlockState checkWaterSideBlockStates(BlockPos blockPos, ClientLevel level) {
        BlockState blockState = level.getBlockState(blockPos.east());
        if (!blockState.is(Blocks.WATER)) return blockState;
        blockState = level.getBlockState(blockPos.west());
        if (!blockState.is(Blocks.WATER)) return blockState;
        blockState = level.getBlockState(blockPos.north());
        if (!blockState.is(Blocks.WATER)) return blockState;
        return level.getBlockState(blockPos.south());
    }

    @Override
    public void disconnectWorld() { }
}

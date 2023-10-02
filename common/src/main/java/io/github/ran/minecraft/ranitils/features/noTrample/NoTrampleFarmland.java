package io.github.ran.minecraft.ranitils.features.noTrample;

import io.github.ran.minecraft.ranitils.interfaces.Eventerface;
import io.github.ran.minecraft.ranitils.mixins.noTrampleAndPickupRange.ServerboundMovePlayerPacketAccessor;
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

    /**
     * The noTrample function is a packet listener that prevents the player from trampling farmland.
     * It does this by checking if the player is on farmland, and if so, it sets their &quot;onGround&quot; value to true.
     * This causes them to be unable to take fall damage while on farmland.

     *
     * @param ServerboundMovePlayerPacket packet Access the onground field
     */
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

    /**
     * The checkBlockStates function is used to check the block state of the player's current position.
     * If it is air, then we will check 8 blocks below until we find a non-air block.
     * This function also checks if the found non-air block is water and if so, it will return a new BlockState with its sideBlockStates checked as well.

     *
     * @param LocalPlayer player Get the player's current position
     * @param ClientLevel level Get the block state of a specific position
     *
     * @return The blockstate of the current position
     */
    private BlockState checkBlockStates(LocalPlayer player, ClientLevel level) {
        final BlockPos blockPos = player.blockPosition();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) {
            int y = #if PRE_MC_1_18_2 net.minecraft.util.Mth.floor(player.getY() - 0.20000000298023224) #else player.getBlockY() #endif;
            BlockPos currentPos;
            for (int i = 0; i < 8; i++) {
                #if PRE_MC_1_18_2
                blockState = level.getBlockState(currentPos = new BlockPos(blockPos.getX(), y - i, blockPos.getZ()));
                #else
                blockState = level.getBlockState(currentPos = blockPos.atY(y - i));
                #endif
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

    /**
     * The checkWaterSideBlockStates function checks the block states of the blocks surrounding a given BlockPos.
     * If any of these blocks are not water, then it returns that block's state. Otherwise, it returns null.

     *
     * @param BlockPos blockPos Get the block state of the water block
     * @param ClientLevel level Get the block state of the surrounding blocks
     *
     * @return A blockstate, which is the blockstate of a block that is not water
     */
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

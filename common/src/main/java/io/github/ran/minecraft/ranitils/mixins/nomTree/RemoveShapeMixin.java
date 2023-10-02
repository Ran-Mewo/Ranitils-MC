package io.github.ran.minecraft.ranitils.mixins.nomTree;

import io.github.ran.minecraft.ranitils.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class RemoveShapeMixin {

    /**
     * The treeNom function is a method injection that allows the player to cut through leaves with an axe.
     * It does this by cancelling the getShape function of LeavesBlock, which returns an empty VoxelShape.
     * This means that when you hit a leaf block with your axe, it will act as if there was nothing there at all!

     *
     * @param BlockGetter blockGetter Get the block state of the block at a given position
     * @param BlockPos blockPos Get the block at that position
     * @param CollisionContext collisionContext Determine if the player is standing on top of the block or not
     * @param CallbackInfoReturnable&lt;VoxelShape&gt; cir Return the voxelshape that is used to determine if a block can be broken
     *
     * @return An empty voxelshape, which makes the block invisible to interactions
     *
     * @docauthor Trelent
     */
    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void treeNom(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null || mc.level == null) return;
//        if (!ModConfig.getInstance().nomTree || collisionContext == CollisionContext.empty()) return;
//        if (!(mc.level.getBlockState(blockPos).getBlock() instanceof LeavesBlock && isHoldingAxe(mc.player))) return;
        if (mc == null || mc.level == null || mc.player == null || !ModConfig.getInstance().nomTree || collisionContext == CollisionContext.empty() || !(mc.level.getBlockState(blockPos).getBlock() instanceof LeavesBlock && isHoldingAxe(mc.player))) return;
        cir.setReturnValue(Shapes.empty());
    }

    @Unique
    private boolean isHoldingAxe(LocalPlayer player) {
        return player.getMainHandItem().getItem() instanceof AxeItem || player.getOffhandItem().getItem() instanceof AxeItem;
    }
}

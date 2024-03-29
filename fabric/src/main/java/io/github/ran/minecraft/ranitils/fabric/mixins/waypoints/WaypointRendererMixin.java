package io.github.ran.minecraft.ranitils.fabric.mixins.waypoints;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;

#if POST_MC_1_16_5 && PRE_MC_1_20_1
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import io.github.ran.minecraft.ranitils.fabric.features.waypoints.Waypoint;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
#endif

@Mixin(LevelRenderer.class)
public abstract class WaypointRendererMixin {
    #if POST_MC_1_16_5 && PRE_MC_1_20_1
    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void render(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        Waypoint.renderWaypoint(poseStack, partialTick);
    }
    #endif
}

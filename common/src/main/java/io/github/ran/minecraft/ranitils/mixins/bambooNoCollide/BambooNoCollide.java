package io.github.ran.minecraft.ranitils.mixins.bambooNoCollide;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.ran.minecraft.ranitils.config.ModConfig;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BambooStalkBlock.class)
public abstract class BambooNoCollide extends BlockBehaviour {
    public BambooNoCollide(Properties properties) {
        super(properties);
    }

    @ModifyReturnValue(method = "getCollisionShape", at = @At("RETURN"))
    public VoxelShape getCollisionShape(VoxelShape original) {
        if (ModConfig.getInstance().bambooNoCollide) return Shapes.empty();
        return original;
    }
}


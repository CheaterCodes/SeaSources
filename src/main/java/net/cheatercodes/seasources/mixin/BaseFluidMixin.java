package net.cheatercodes.seasources.mixin;

import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blocks.WaterStrainerNet;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin extends Fluid {
    @Redirect(method = "onScheduledTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    public boolean setBlockStateProxy(World world, BlockPos blockPos, BlockState blockState, int flags)
    {
        BlockState oldState = world.getBlockState(blockPos);
        if(oldState.getBlock() == SeaSources.WATER_STRAINER_NET) {
            return world.setBlockState(blockPos, oldState.with(WaterStrainerNet.WATER_LEVEL, blockState.getFluidState().getLevel()), flags);
        }
        return world.setBlockState(blockPos, blockState, flags);
    }
}

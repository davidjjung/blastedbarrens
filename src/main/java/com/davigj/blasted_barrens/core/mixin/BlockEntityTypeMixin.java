package com.davigj.blasted_barrens.core.mixin;

import com.davigj.blasted_barrens.core.registry.BBBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Inject(at = @At("HEAD"), method = "isValid", cancellable = true)
    private void S$isValid(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(BBBlocks.SUSPICIOUS_ASHEN_SAND.get()) && BlockEntityType.BRUSHABLE_BLOCK.equals(this)) {
            cir.setReturnValue(true);
        }
    }
}

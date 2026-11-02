package com.restonic4.banishedmobs.mixin;

import com.restonic4.banishedmobs.Banishment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
	@Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
	private void banishedMobs$refuse(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (Banishment.isBanished(entity)) { cir.setReturnValue(false); }
	}
}

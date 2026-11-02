package com.restonic4.banishwhimsicalmobs.mixin;

import com.restonic4.banishwhimsicalmobs.Banishment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
	// Refusing here, before the mob is built, keeps Mob.finalizeSpawn from adding jockey mounts for a mob that is then refused.
	@Inject(method = "isValidSpawnPostitionForType", at = @At("HEAD"), cancellable = true)
	private static void banishWhimsicalMobs$skip(
		ServerLevel level,
		MobCategory category,
		StructureManager structures,
		ChunkGenerator generator,
		MobSpawnSettings.SpawnerData spawner,
		BlockPos.MutableBlockPos pos,
		double distanceSq,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (Banishment.isBanished(spawner.type)) { cir.setReturnValue(false); }
	}
}

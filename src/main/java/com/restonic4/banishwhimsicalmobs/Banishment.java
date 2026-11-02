package com.restonic4.banishwhimsicalmobs;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public final class Banishment {
	public static final TagKey<EntityType<?>> BANISHED = TagKey.create(Registries.ENTITY_TYPE, BanishWhimsicalMobs.id("banished"));

	private Banishment() {}

	public static void register() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> discardIfBanished(entity));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resources, success) -> sweep(server));
	}

	public static boolean isBanished(EntityType<?> type) {
		return type.is(BANISHED);
	}

	public static boolean isBanished(Entity entity) {
		return !(entity instanceof Player) && isBanished(entity.getType());
	}

	public static boolean discardIfBanished(Entity entity) {
		if (!isBanished(entity)) { return false; }

		entity.discard();
		return true;
	}

	public static int discardBanished(Iterable<? extends Entity> entities) {
		List<Entity> snapshot = new ArrayList<>();
		entities.forEach(snapshot::add);

		int discarded = 0;
		for (Entity entity : snapshot) {
			if (discardIfBanished(entity)) { discarded++; }
		}

		return discarded;
	}

	private static void sweep(MinecraftServer server) {
		int discarded = 0;
		for (ServerLevel level : server.getAllLevels()) {
			discarded += discardBanished(level.getAllEntities());
		}

		if (discarded > 0) {
			BanishWhimsicalMobs.LOGGER.info("Data reload banished {} loaded entities", discarded);
		}
	}
}

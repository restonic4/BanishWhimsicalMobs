package com.restonic4.banishedmobs.gametest;

import com.restonic4.banishedmobs.Banishment;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class BanishmentGameTests implements FabricGameTest {
	private static final List<EntityType<?>> BANISHED = List.of(
		EntityType.ZOMBIE, EntityType.HUSK, EntityType.DROWNED, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOGLIN,
		EntityType.ZOMBIE_HORSE, EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED, EntityType.WITHER_SKELETON, EntityType.SKELETON_HORSE,
		EntityType.PHANTOM, EntityType.WITHER, EntityType.WITCH, EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.VEX,
		EntityType.WARDEN, EntityType.GHAST, EntityType.BLAZE
	);
	private static final List<EntityType<?>> KEPT = List.of(
		EntityType.COW, EntityType.CREEPER, EntityType.SPIDER, EntityType.VILLAGER, EntityType.ENDERMAN, EntityType.PILLAGER,
		EntityType.ENDER_DRAGON, EntityType.IRON_GOLEM, EntityType.SLIME
	);

	private final List<String> failures = new ArrayList<>();

	@GameTest(template = EMPTY_STRUCTURE)
	public void shippedList(GameTestHelper helper) {
		for (EntityType<?> type : BANISHED) {
			expect(type + " is banished", Banishment.isBanished(type));
		}
		for (EntityType<?> type : KEPT) {
			expect(type + " is kept", !Banishment.isBanished(type));
		}
		finish(helper);
	}

	@GameTest(template = EMPTY_STRUCTURE)
	public void freshSpawnsAreRefused(GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Vec3 at = helper.absolutePos(new BlockPos(1, 2, 1)).getCenter();
		for (EntityType<?> type : BANISHED) {
			Entity entity = type.create(level);
			entity.moveTo(at);
			expect(type + " addFreshEntity refused", !level.addFreshEntity(entity));
			expect(type + " never reached the level", level.getEntity(entity.getUUID()) == null);
		}
		for (EntityType<?> type : KEPT) {
			if (type == EntityType.ENDER_DRAGON) { continue; }
			Entity entity = type.create(level);
			entity.moveTo(at);
			expect(type + " addFreshEntity accepted", level.addFreshEntity(entity));
			entity.discard();
		}
		finish(helper);
	}

	@GameTest(template = EMPTY_STRUCTURE)
	public void loadEventDiscards(GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		for (EntityType<?> type : BANISHED) {
			Entity entity = type.create(level);
			ServerEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, level);
			expect(type + " discarded on load", entity.isRemoved());
		}
		for (EntityType<?> type : KEPT) {
			Entity entity = type.create(level);
			ServerEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, level);
			expect(type + " survives load", !entity.isRemoved());
		}
		finish(helper);
	}

	@GameTest(template = EMPTY_STRUCTURE)
	public void sweepRemovesOnlyBanished(GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		List<Entity> mixed = new ArrayList<>();
		for (EntityType<?> type : BANISHED) { mixed.add(type.create(level)); }
		for (EntityType<?> type : KEPT) { mixed.add(type.create(level)); }

		int discarded = Banishment.discardBanished(mixed);

		expect("sweep count", discarded == BANISHED.size());
		for (Entity entity : mixed) {
			boolean shouldGo = BANISHED.contains(entity.getType());
			expect(entity.getType() + (shouldGo ? " swept" : " left alone"), entity.isRemoved() == shouldGo);
		}
		finish(helper);
	}

	private void expect(String what, boolean ok) {
		if (!ok) { failures.add(what); }
	}

	private void finish(GameTestHelper helper) {
		if (failures.isEmpty()) {
			helper.succeed();
		} else {
			String message = String.join("; ", failures);
			failures.clear();
			helper.fail(message);
		}
	}
}

package com.restonic4.banishwhimsicalmobs;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BanishWhimsicalMobs implements ModInitializer {
	public static final String MOD_ID = "banish_whimsical_mobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		Banishment.register();
	}
}

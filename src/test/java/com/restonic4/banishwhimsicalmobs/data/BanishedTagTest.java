package com.restonic4.banishwhimsicalmobs.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class BanishedTagTest {
	private static final Pattern ENTRY = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9_./-]+");

	private static JsonObject shipped() throws Exception {
		try (InputStream in = BanishedTagTest.class.getResourceAsStream("/data/banish_whimsical_mobs/tags/entity_type/banished.json")) {
			assertNotNull(in);
			return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
		}
	}

	@Test
	void everyEntryIsAnIdOrATagReference() throws Exception {
		for (JsonElement element : shipped().getAsJsonArray("values")) {
			assertTrue(ENTRY.matcher(element.getAsString()).matches(), element.getAsString());
		}
	}

	@Test
	void noEntryIsListedTwice() throws Exception {
		Set<String> seen = new HashSet<>();
		for (JsonElement element : shipped().getAsJsonArray("values")) {
			assertTrue(seen.add(element.getAsString()), "duplicate " + element.getAsString());
		}
	}

	@Test
	void shippedTagMergesWithOtherPacksInsteadOfReplacingThem() throws Exception {
		assertFalse(shipped().get("replace").getAsBoolean());
	}

	@Test
	void theUndeadComeFromVanillasOwnTagSoNewUndeadAreCovered() throws Exception {
		Set<String> values = new HashSet<>();
		shipped().getAsJsonArray("values").forEach(e -> values.add(e.getAsString()));
		assertTrue(values.contains("#minecraft:undead"));
		assertEquals(Set.of("#minecraft:undead"), values.stream().filter(v -> v.startsWith("#")).collect(java.util.stream.Collectors.toSet()));
	}
}

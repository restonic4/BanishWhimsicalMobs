# Banish Whimsical Mobs

A Fabric mod for Minecraft **1.21.1** that removes whimsical mobs from the game.

By default these are banned:

- every zombie and skeleton type (zombie, husk, drowned, zombie villager, zombified piglin, zoglin, zombie horse, skeleton, stray, bogged, wither skeleton, skeleton horse), the wither and the phantom
- witch, evoker, illusioner, vex, warden, ghast, blaze
- creeper, enderman

Everything else (spiders, pillagers, villagers, animals, the ender dragon, ...) is the same.

The list is not hard-coded. You change it with a small datapack, no coding needed.

---

## Changing the list with a datapack

A datapack is a folder you drop into a world. It belongs to **one world only**: do the steps again for every world (or server) you want to change.

### 1. Open your world's folder

- **Singleplayer:** in the world selection screen, select your world, click **Edit**, then **Open World Folder**.
- **Server:** open the server's main folder, then the folder named after `level-name` in `server.properties` (it is `world` unless you changed it).

### 2. Open the `datapacks` folder

It is inside the world folder. If it is not there, create it. The name is all lowercase: `datapacks`.

### 3. Create your pack folder

Inside `datapacks`, create a new folder. The name is yours to choose, for example `my_banish_list`. Avoid spaces.

### 4. Create `pack.mcmeta`

Inside your pack folder, create a text file named exactly `pack.mcmeta` with this content:

```json
{
  "pack": {
    "pack_format": 48,
    "description": "My banish list"
  }
}
```

`48` is the datapack version of Minecraft 1.21.1. Keep it as is.

**Windows warning:** by default Windows hides file extensions, and Notepad likes to save `pack.mcmeta` as `pack.mcmeta.txt`. In File Explorer, enable *View > Show > File name extensions* and check that the file is really called `pack.mcmeta`. In Notepad's save dialog, set "Save as type" to **All files**. The same warning applies to `banished.json` below.

If you are on a real operating system like Linux, you probably won't have this problem.

### 5. Create the folders for the list

Inside your pack folder, create this chain of folders, spelled exactly like this, and put the file `banished.json` in the last one:

```
my_banish_list/
├─ pack.mcmeta
└─ data/
   └─ banish_whimsical_mobs/
      └─ tags/
         └─ entity_type/
            └─ banished.json
```

Watch out: `banish_whimsical_mobs` has underscores, `entity_type` is singular, and the file is `banished.json`. A typo in any name makes the game silently ignore the file.

### 6. Write the list in `banished.json`

Pick one of these three.

**Add more mobs to the default list** (here the endermite and every raider):

```json
{
  "values": [
    "minecraft:endermite",
    "#minecraft:raiders"
  ]
}
```

**Remove mobs from the default list, or build your own list from scratch.** Add `"replace": true` and write the **complete** list you want. The default list is thrown away, so anything you don't write here is allowed again. This example bans only witches and creepers:

```json
{
  "replace": true,
  "values": [
    "minecraft:witch",
    "minecraft:creeper"
  ]
}
```

**Allow everything again** (the mod does nothing):

```json
{
  "replace": true,
  "values": []
}
```

#### How to write the entries

- A single mob: `"minecraft:zombie"`. This is `minecraft:` followed by the mob's name in lowercase with underscores (`minecraft:wither_skeleton`, `minecraft:zombie_villager`). If you are unsure, type `/summon ` in the game chat and the list of names appears as you type.
- A whole group: a name starting with `#`, for example `"#minecraft:undead"`. Useful groups: `#minecraft:undead`, `#minecraft:zombies`, `#minecraft:skeletons`, `#minecraft:illager`, `#minecraft:raiders`.
- A mob from another mod, written as `{ "id": "somemod:ghoul", "required": false }`. The `required: false` part stops the pack from failing when that mod is not installed.
- Entries are separated by commas. **No comma after the last entry.** Use plain straight quotes `"`, not curly ones.

### 7. Apply it

- **World already running:** type `/reload` in chat (you need cheats or operator rights). Mobs that are now banned and already loaded disappear immediately.
- **Otherwise:** just open the world / start the server.

### 8. Check that it worked

Type `/datapack list`. Your pack must appear as `file/my_banish_list` in the enabled list. If it does not, the folder is in the wrong place or `pack.mcmeta` is wrong (usually the `.txt` problem above). If Minecraft reports an error when loading, there is a typo in a JSON file; read the message, fix it and use `/reload` again.

---

## Good to know

- **Banning removes mobs gradually in saved areas.** Mobs standing in chunks that are not loaded are removed when those chunks load (when you get close), not all at once.
- **Unbanning does not bring mobs back.** Removed mobs are gone; they start spawning naturally again.
- **Spawn eggs and `/summon`** also stop working for banned mobs. The `/summon` command still prints "Summoned new ...", but nothing appears.
- **Spawners** (for example a zombie spawner in a dungeon) keep running but produce nothing.
- **Jockeys:** a banned rider on an allowed mount (a skeleton on a spider) does not appear; the mount does.
- The mod's own list lives in the jar at `data/banish_whimsical_mobs/tags/entity_type/banished.json`. You never need to edit the jar.

## License

MIT, see [LICENSE](LICENSE).

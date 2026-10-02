<img width="768" height="456" alt="banner" src="https://github.com/user-attachments/assets/b4b89ece-8010-4621-9363-ac1ca0a77dfd" />

A Curios necklace that makes nearby mob spawners run faster.

Wear the **Spawner Necklace** in a curio slot and spawners around you will spawn faster.
Right-click it in hand to switch it off and on.

Requires [Curios API.](https://www.curseforge.com/minecraft/mc-mods/curios)

## Behavior

- Speeds up every spawner within range of the wearer.
- Optional progression: the necklace can start weak and level up from mob kills, breaking spawners, and xp.

## Obtaining

By default the necklace is **not craftable**. it can be found in chest loot, in dungeons, mineshafts, strongholds, fortresses, bastions, mansions and ancient cities chests. It is 5% chance by default.

Set `spawner_necklace.craftable = true` to enable the recipe and then `/reload`.

The `loot.tables` list accepts modded loot tables:

| Form | Matches |
|------|---------|
| `minecraft:chests/simple_dungeon` | this one table |
| `minecraft:chests/*` | every table under that path |
| `@somemod` | every loot table added by a mod |

## Slots

The necklace by default supports the `necklace` and `charm` Curios slots.
Leave the list empty to allow any slot.

## Necklaces

There are four craftable tiers of the Spawner Necklace. They are off by default. Set `enabled = true` to turn them on. Tiers don't use progression.

| Section | Speed | Range |
|---------|-------|-------|
| `iron_spawner_necklace` | 1.5x | 12 |
| `gold_spawner_necklace` | 2x | 16 |
| `diamond_spawner_necklace` | 5x | 24 |
| `netherite_spawner_necklace` | 10x | 32 |

Every necklace section, `spawner_necklace` included, has these keys:

| Key | Purpose |
|-----|--------------|
| `enabled` | Turns the item on. When off it does nothing and is hidden from the creative tab. |
| `speed_multiplier` | Spawner speed. |
| `range` | Range in blocks. |
| `craftable` | Allows the recipe. `/reload` after changes. |
| `in_loot` | Lets it show up in the `loot.tables` chests. |

## Config

`config/spawnercurio-common.toml`, is grouped into `general`, `slots`, `toggle`, `item`, `progression`,
`spawner`, `filters`, `loot`, and one section per necklace.

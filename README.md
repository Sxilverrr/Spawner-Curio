# Spawner Curio

A Curios necklace that makes nearby mob spawners run faster.

Wear the **Spawner Necklace** in a curio slot and spawners around you will spawn faster.
Right-click it in hand to switch it off and on.

## Behavior

- Speeds up every spawner within range of the wearer.
- Optional progression: the necklace can start weak and level up from mob kills, breaking spawners, and xp.

## Obtaining

By default the necklace is **not craftable**. it can be found in chest loot, in dungeons, mineshafts, strongholds, fortresses, bastions, mansions and ancient cities chests. It is 5% chance by default.

Set `crafting.enabled = true` to enable the recipe and then `/reload`.

The `loot.tables` list accepts modded loot tables:

| Form | Matches |
|------|---------|
| `minecraft:chests/simple_dungeon` | this one table |
| `minecraft:chests/*` | every table under that path |
| `@somemod` | every loot table added by a mod |

## Slots

The necklace by default supports the `necklace` and `charm` Curios slots.
Leave the list empty to allow any slot.

## Config

`config/spawnercurio-common.toml`, is grouped into `general`, `slots`, `toggle`, `item`, `progression`,
`spawner`, and `filters`.

# ForestAttack
ForestAttack is a Minecraft game mode based on the German YouTuber project CraftAttack.
The main task is to play through Minecraft in survival mode within a year
and achieve the highest possible progress in the game
(for example, by building fully automated farms).
This plugin is intended to make the game mode playable for communities and adds helpful functions.

## Compatibility
The plugin was developed based on the [Paper](https://github.com/PaperMC/Paper) server software,
which is required for operation.
The free plugins [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) and [LuckPerms](https://github.com/LuckPerms/LuckPerms) as well as the paid plugin [PremiumVanish](https://www.spigotmc.org/resources/premiumvanish-stay-hidden-bungee-velocity-support.14404/) are also required
to use the plugin.
For the best possible game flow,
we recommend this [server optimization tutorial](https://github.com/YouHaveTrouble/minecraft-optimization).

## Functions
* Chat functions (Chat Format, Tablist, Prefixes, Join/Leave messages)
* Commands (Spawn, Shop, Biome, Chunkborder, Enderchest)
* Spawn protection
* Store function (in development)
* PlaceholderAPI extension

## Permissions
| Permission                             | Description                                        |
|----------------------------------------|----------------------------------------------------|
| forestattack.commands.enderchest       | Opens your own Enderchest.                         |
| forestattack.commands.enderchest.other | Opens the Enderchest of another player.            |
| forestattack.commands.spawn.bypass     | Bypasses the teleport timer during spawn teleport. |
| forestattack.commands.shop.bypass      | Bypasses the teleport timer during shop teleport.  |

## Placeholders
| Placeholder               | Description                                              |
|---------------------------|----------------------------------------------------------|
| name_prefixed_and_colored | The colored player name with its permission group prefix |
| name_colored              | The colored player name                                  |

## Setting up permission groups
For optimum functionality, it is recommended to set the permission groups. To do this, the group prefix, priority and the meta-value color must be set. The lower the group weight, the higher the player is displayed in the table list. This also has the same effect on the player priority in commands. For more information on how-to setup permission groups using the LuckPerms permissions plugin, refer to the [official documentation](https://luckperms.net/wiki/Home).
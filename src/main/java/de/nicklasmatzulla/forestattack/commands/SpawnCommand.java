/*
 * MIT License
 *
 * Copyright (c) 2024 Nicklas Matzulla
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.nicklasmatzulla.forestattack.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.nicklasmatzulla.forestattack.config.LocationsConfig;
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.util.LocationsUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage", "SameReturnValue"})
@AllArgsConstructor
public class SpawnCommand {
    private final MessagesConfig messagesConfig;
    private final LocationsConfig locationsConfig;
    private final LocationsUtil locationsUtil;

    public @NotNull LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("spawn")
                .requires(this::checkBasePermission)
                .executes(this::executeBase)
                .build();
    }

    private boolean checkBasePermission(final CommandSourceStack source) {
        return true;
    }

    private int executeBase(final @NotNull CommandContext<CommandSourceStack> context) {
        final CommandSender commandSender = context.getSource().getSender();
        if (!(commandSender instanceof final Player player)) {
            final Component onlyPlayerMessageComponent = this.messagesConfig.getOnlyPlayerMessageComponent();
            commandSender.sendMessage(onlyPlayerMessageComponent);
            return Command.SINGLE_SUCCESS;
        }
        final Location spawnPointLocation = this.locationsConfig.getSpawnSpawnPointLocation();
        if (player.hasPermission("forestattack.commands.spawn.bypass")) {
            player.teleport(spawnPointLocation);
            return Command.SINGLE_SUCCESS;
        }
        this.locationsUtil.teleportWithTimer(player, spawnPointLocation);
        return Command.SINGLE_SUCCESS;
    }

}

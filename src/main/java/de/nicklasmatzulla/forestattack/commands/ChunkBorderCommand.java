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
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings({"UnstableApiUsage", "SameReturnValue"})
public class ChunkBorderCommand {
    private final MessagesConfig messagesConfig;
    private final List<UUID> enabledPlayers = Collections.synchronizedList(new ArrayList<>());

    public ChunkBorderCommand(final @NotNull Plugin plugin, final @NotNull MessagesConfig messagesConfig) {
        this.messagesConfig = messagesConfig;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::handleShowChunkBorders, 0, 5);
    }

    public @NotNull LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("chunkborder")
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
        final UUID uniqueId = player.getUniqueId();
        if (!enabledPlayers.remove(uniqueId)) {
            enabledPlayers.add(uniqueId);
            final Component chunkBorderEnabledMessageComponent = this.messagesConfig.getChunkBorderEnabledComponent();
            player.sendMessage(chunkBorderEnabledMessageComponent);
            return Command.SINGLE_SUCCESS;
        }
        final Component chunkBorderDisabledComponent = this.messagesConfig.getChunkBorderDisabledComponent();
        player.sendMessage(chunkBorderDisabledComponent);
        return Command.SINGLE_SUCCESS;
    }

    private void handleShowChunkBorders() {
        final Iterator<UUID> iterator = this.enabledPlayers.iterator();
        while (iterator.hasNext()) {
            final UUID uniqueId = iterator.next();
            final Player player = Bukkit.getPlayer(uniqueId);
            if (player == null) {
                iterator.remove();
                continue;
            }
            final Chunk chunk = player.getLocation().getChunk();
            final int chunkX = chunk.getX() << 4;
            final int chunkZ = chunk.getZ() << 4;
            // Show particles along the borders and edges of the chunk for the specific player
            for (int y = -64; y <= 320; y += 2) { // Vertical lines every 2 blocks
                for (int i = 0; i <= 15; i++) { // Horizontal lines every blocks
                    // North border (horizontal line)
                    player.spawnParticle(Particle.FLAME, chunkX + i + 0.5, y, chunkZ + 0.5, 0, 0, 0, 0, 1);
                    // South border (horizontal line)
                    player.spawnParticle(Particle.FLAME, chunkX + i + 0.5, y, chunkZ + 15 + 0.5, 0, 0, 0, 0, 1);
                    // West border (vertical line)
                    player.spawnParticle(Particle.FLAME, chunkX + 0.5, y, chunkZ + i + 0.5, 0, 0, 0, 0, 1);
                    // East border (vertical line)
                    player.spawnParticle(Particle.FLAME, chunkX + 15 + 0.5, y, chunkZ + i + 0.5, 0, 0, 0, 0, 1);
                }
            }
        }
    }

}

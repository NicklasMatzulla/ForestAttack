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

package de.nicklasmatzulla.forestattack.chat;

import de.myzelyam.api.vanish.VanishAPI;
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.util.LuckPermsUtil;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
@SuppressWarnings("ConstantValue") // Because PremiumVanish API (always returns false - code is not the implementation)
public class TabListStyle {
    private final Plugin plugin;
    private final MessagesConfig messagesConfig;

    public void refreshHeaderFooter() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            final int maxPlayers = Bukkit.getMaxPlayers();
            Bukkit.getOnlinePlayers().forEach(player -> {
                final int onlinePlayers = Math.toIntExact(Bukkit.getOnlinePlayers().stream()
                        .filter(onlinePlayer -> VanishAPI.canSee(player, onlinePlayer))
                        .count());
                final Component headerComponent = messagesConfig.getTabListHeaderComponent(player, onlinePlayers, maxPlayers);
                final Component footerComponent = messagesConfig.getTabListFooterComponent(player, onlinePlayers, maxPlayers);
                player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
            });
        });
    }

    public void refreshPlayer(final @NotNull Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            final UUID playerUniqueId = player.getUniqueId();
            final int playerWeight = LuckPermsUtil.getWeight(playerUniqueId);
            final boolean isPlayerVanished = VanishAPI.isInvisible(player);
            final Component playerPrefix = LuckPermsUtil.getPrefix(playerUniqueId);
            final NamedTextColor playerColor = LuckPermsUtil.getColor(playerUniqueId);
            final NamedTextColor vanishedColor = messagesConfig.getVanishedNameTagColor();
            final Component vanishedSuffixComponent = messagesConfig.getVanishedSuffixComponent();
            // Process each online player
            for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                updatePlayerTeam(player, onlinePlayer, playerWeight, playerPrefix, playerColor, isPlayerVanished, vanishedSuffixComponent, vanishedColor);
                updatePlayerTeam(onlinePlayer, player, LuckPermsUtil.getWeight(onlinePlayer.getUniqueId()), LuckPermsUtil.getPrefix(onlinePlayer.getUniqueId()), LuckPermsUtil.getColor(onlinePlayer.getUniqueId()), VanishAPI.isInvisible(onlinePlayer), vanishedSuffixComponent, vanishedColor);
            }
        });
    }

    private void updatePlayerTeam(final @NotNull Player target, final @NotNull Player viewer, final int weight, final @NotNull Component prefix, final @NotNull NamedTextColor color, final boolean isVanished, final @NotNull Component suffix, final @NotNull NamedTextColor vanishedColor) {
        final Scoreboard targetScoreboard = target.getScoreboard();
        final Scoreboard viewerScoreboard = viewer.getScoreboard();
        // Unregister existing team for the viewer
        unregisterTeam(viewerScoreboard, target);
        if (VanishAPI.canSee(viewer, target)) {
            final String teamName = getTeamName(weight, target.getName(), isVanished);
            Team team = viewerScoreboard.getTeam(teamName);
            if (team == null) {
                team = viewerScoreboard.registerNewTeam(teamName);
            }
            team.prefix(prefix);
            if (isVanished) {
                team.suffix(suffix);
            }
            team.color(isVanished ? vanishedColor : color);
            team.addPlayer(target);
        }
        // Unregister existing team for the target
        unregisterTeam(targetScoreboard, viewer);
        if (VanishAPI.canSee(target, viewer)) {
            final String teamName = getTeamName(LuckPermsUtil.getWeight(viewer.getUniqueId()), viewer.getName(), VanishAPI.isInvisible(viewer));
            Team team = targetScoreboard.getTeam(teamName);
            if (team == null) {
                team = targetScoreboard.registerNewTeam(teamName);
            }
            team.prefix(LuckPermsUtil.getPrefix(viewer.getUniqueId()));
            if (VanishAPI.isInvisible(viewer)) {
                team.suffix(suffix);
            }
            team.color(VanishAPI.isInvisible(viewer) ? vanishedColor : LuckPermsUtil.getColor(viewer.getUniqueId()));
            team.addPlayer(viewer);
        }
    }

    private @NotNull String getTeamName(final int weight, final @NotNull String playerName, final boolean isVanished) {
        return (isVanished ? "-" : "") + String.format("%03d", weight) + playerName;
    }

    private void unregisterTeam(final @NotNull Scoreboard scoreboard, final @NotNull Player player) {
        final Team team = scoreboard.getPlayerTeam(player);
        if (team != null) {
            team.unregister();
        }
    }

    public void unregisterPlayer(final @NotNull Player player) {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            unregisterTeam(onlinePlayer.getScoreboard(), player);
        }
    }
}

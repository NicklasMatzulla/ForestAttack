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

package de.nicklasmatzulla.forestattack.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class LuckPermsUtil {

    private static final LuckPerms API = LuckPermsProvider.get();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final String ALTERNATIVE_PREFIX = "<red>prefix_not_found</red>";
    private static final NamedTextColor ALTERNATIVE_COLOR = NamedTextColor.GRAY;

    public static int getWeight(final @NotNull UUID uniqueId) {
        final User user = API.getUserManager().getUser(uniqueId);
        final String groupName = user == null ? "default" : user.getPrimaryGroup();
        final Group group = API.getGroupManager().getGroup(groupName);
        return Objects.requireNonNull(group).getWeight().orElse(0);
    }

    public static @NotNull Component getPrefix(final @NotNull UUID uniqueId) {
        final User user = API.getUserManager().getUser(uniqueId);
        final String groupName = user == null ? "default" : user.getPrimaryGroup();
        final Group group = API.getGroupManager().getGroup(groupName);
        final String rawPrefix = Objects.requireNonNull(group).getCachedData().getMetaData().getPrefix();
        return MINI_MESSAGE.deserialize(rawPrefix == null ? ALTERNATIVE_PREFIX : rawPrefix);
    }

    public static @NotNull NamedTextColor getColor(final @NotNull UUID uniqueId) {
        final User user = API.getUserManager().getUser(uniqueId);
        final String groupName = user == null ? "default" : user.getPrimaryGroup();
        final Group group = API.getGroupManager().getGroup(groupName);
        final String rawColor = Objects.requireNonNull(group).getCachedData().getMetaData().getMetaValue("color");
        return NamedTextColor.NAMES.valueOr(rawColor == null ? "gray" : rawColor.toLowerCase(), ALTERNATIVE_COLOR);
    }

    public static @NotNull Component getColoredPlayerName(final @NotNull OfflinePlayer player) {
        final UUID uniqueId = player.getUniqueId();
        final String playerName = player.getName() == null ? "Unknown" : player.getName();
        final NamedTextColor color = getColor(uniqueId);
        return Component.text(playerName).color(color);
    }

    public static @NotNull Component getPrefixedAndColoredPlayerName(final @NotNull OfflinePlayer player) {
        final UUID uniqueId = player.getUniqueId();
        final String playerName = player.getName() == null ? "Unknown" : player.getName();
        final Component prefix = getPrefix(uniqueId);
        final NamedTextColor color = getColor(uniqueId);
        return prefix.append(Component.text(playerName).color(color));
    }

    public static List<Player> getModifiablePlayers(final @NotNull Player checkedPlayer) {
        final int checkedPlayerWeight = getWeight(checkedPlayer.getUniqueId());
        return Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> checkedPlayerWeight < getWeight(onlinePlayer.getUniqueId()))
                .collect(Collectors.toList());
    }

}

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

package de.nicklasmatzulla.forestattack.config;

import de.nicklasmatzulla.forestattack.config.util.BaseConfig;
import de.nicklasmatzulla.forestattack.util.LuckPermsUtil;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

@SuppressWarnings("unused")
@Getter
public class MessagesConfig extends BaseConfig {
    // General
    @Getter(AccessLevel.NONE)
    private final Component prefixComponent;
    private final Component onlyPlayerMessageComponent;
    private final Component playerNotFoundMessageComponent;
    private final Component unsupportedOperationMessageComponent;
    private final Component enabledPatternComponent;
    private final Component disabledPatternComponent;
    // Error
    private final Component errorLoadDataKickComponent;
    // Chat
    private final String chatFormat;
    private final NamedTextColor vanishedNameTagColor;
    private final Component vanishedSuffixComponent;
    // Commands
    private final Component chunkBorderEnabledComponent;
    private final Component chunkBorderDisabledComponent;
    private final Component enderchestOpenedSelfMessageComponent;
    private final Component shopDisabledMessageComponent;
    private final Component shopUsageMessageComponent;
    private final Component kitClaimedMessageComponent;
    private final Component kitAlreadyClaimedMessageComponent;

    public MessagesConfig(final @NotNull Logger logger) {
        super(logger, new File("plugins/ForestAttack/messages.yml"), "config/messages.yml", true);
        // General
        this.prefixComponent = getComponent("general.prefix");
        this.onlyPlayerMessageComponent = getPrefixedComponent("general.onlyPlayers");
        this.playerNotFoundMessageComponent = getPrefixedComponent("general.playerNotFound");
        this.unsupportedOperationMessageComponent = getPrefixedComponent("general.unsupportedOperation");
        this.enabledPatternComponent = getComponent("general.patterns.enabled");
        this.disabledPatternComponent = getComponent("general.patterns.disabled");
        // Error
        this.errorLoadDataKickComponent = getComponent("errors.kick.failedLoadData");
        // Chat
        this.chatFormat = this.config.getString("features.chat.chatFormat", "<red>chatFormat</red>");
        this.vanishedNameTagColor = NamedTextColor.NAMES.valueOr(this.config.getString("features.chat.vanishedNameTagColor", "YELLOW"), NamedTextColor.YELLOW);
        this.vanishedSuffixComponent = getComponent("features.chat.vanishedSuffix");
        // Commands
        this.chunkBorderEnabledComponent = getPrefixedComponent("commands.chunkBorders.enabled");
        this.chunkBorderDisabledComponent = getPrefixedComponent("commands.chunkBorders.disabled");
        this.enderchestOpenedSelfMessageComponent = getPrefixedComponent("commands.enderchest.openedSelf");
        this.shopDisabledMessageComponent = getPrefixedComponent("commands.shop.disabled");
        this.shopUsageMessageComponent = getPrefixedComponent("commands.shop.usage");
        this.kitClaimedMessageComponent = getPrefixedComponent("commands.kit.claimed");
        this.kitAlreadyClaimedMessageComponent = getPrefixedComponent("commands.kit.alreadyClaimed");
    }

    public @NotNull Component getKickMessageComponent(final @NotNull Component kickReason) {
        final TagResolver kickReasonPlaceholder = Placeholder.component("kick_reason", kickReason);
        return getComponent("general.kickMessage", kickReasonPlaceholder);
    }

    public @NotNull Component getTabListHeaderComponent(final @NotNull Player player, final int onlinePlayers, final int maxPlayers) {
        final TagResolver onlinePlayersPlaceholder = Placeholder.unparsed("online_players", String.valueOf(onlinePlayers));
        final TagResolver maxPlayersPlaceholder = Placeholder.unparsed("max_players", String.valueOf(maxPlayers));
        return getComponent(player, "features.chat.header", onlinePlayersPlaceholder, maxPlayersPlaceholder);
    }

    public @NotNull Component getTabListFooterComponent(final @NotNull Player player, final int onlinePlayers, final int maxPlayers) {
        final TagResolver onlinePlayersPlaceholder = Placeholder.unparsed("online_players", String.valueOf(onlinePlayers));
        final TagResolver maxPlayersPlaceholder = Placeholder.unparsed("max_players", String.valueOf(maxPlayers));
        return getComponent(player, "features.chat.footer", onlinePlayersPlaceholder, maxPlayersPlaceholder);
    }

    public Component getJoinMessageComponent(final @NotNull Player player) {
        final Component coloredPlayerName = LuckPermsUtil.getColoredPlayerName(player);
        final TagResolver playerNameTagResolver = Placeholder.component("name_colored", coloredPlayerName);
        return getPrefixedComponent("features.chat.joinMessage", playerNameTagResolver);
    }

    public Component getQuitMessageComponent(final @NotNull Player player) {
        final Component coloredPlayerName = LuckPermsUtil.getColoredPlayerName(player);
        final TagResolver playerNameTagResolver = Placeholder.component("name_colored", coloredPlayerName);
        return getPrefixedComponent("features.chat.quitMessage", playerNameTagResolver);
    }

    public Component getBiomeMessageComponent(final @NotNull TranslatableComponent biomeComponent) {
        final TagResolver biomePlaceholder = Placeholder.component("biome_name_translated", biomeComponent);
        return getPrefixedComponent("commands.biome", biomePlaceholder);
    }

    public Component getEnderchestOpenedOtherMessageComponent(final @NotNull OfflinePlayer victim) {
        final Component coloredPlayerName = LuckPermsUtil.getColoredPlayerName(victim);
        final TagResolver playerNameTagResolver = Placeholder.component("victim_name_colored", coloredPlayerName);
        return getPrefixedComponent("commands.enderchest.openedOther", playerNameTagResolver);
    }

    public Component getEnderchestFailedMessageComponent(final @NotNull OfflinePlayer victim) {
        final Component coloredPlayerName = LuckPermsUtil.getColoredPlayerName(victim);
        final TagResolver playerNameTagResolver = Placeholder.component("victim_name_colored", coloredPlayerName);
        return getPrefixedComponent("commands.enderchest.failed", playerNameTagResolver);
    }

    public Component getEnderchestOfflinePlayerTitleComponent(final @NotNull OfflinePlayer victim) {
        final String victimName = victim.getName() == null ? "Unknown" : victim.getName();
        final TagResolver.Single playerNameTagResolver = Placeholder.unparsed("victim_name", victimName);
        return getComponent("commands.enderchest.offlinePlayerTitle", playerNameTagResolver);
    }

    public Component getShopStatusMessageComponent(final boolean status) {
        final Component statusComponent = status ? this.enabledPatternComponent : this.disabledPatternComponent;
        final TagResolver statusPlaceholder = Placeholder.component("status", statusComponent);
        return getPrefixedComponent("commands.shop.updateStatus", statusPlaceholder);
    }

    private @NotNull Component getPrefixedComponent(final @NotNull OfflinePlayer offlinePlayer, final @NotNull String key, final @NotNull TagResolver... tagResolvers) {
        final Component component = getComponent(offlinePlayer, key, tagResolvers);
        return this.prefixComponent.append(component);
    }

    private @NotNull Component getPrefixedComponent(final @NotNull String key, final @NotNull TagResolver... tagResolvers) {
        final Component component = getComponent(key, tagResolvers);
        return this.prefixComponent.append(component);
    }
}

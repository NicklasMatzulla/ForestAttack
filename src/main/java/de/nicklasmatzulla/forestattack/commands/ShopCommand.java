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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.nicklasmatzulla.forestattack.config.LocationsConfig;
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.config.SettingsConfig;
import de.nicklasmatzulla.forestattack.util.LocationsUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"UnstableApiUsage", "SameReturnValue"})
@AllArgsConstructor
public class ShopCommand {
    private final SettingsConfig settingsConfig;
    private final MessagesConfig messagesConfig;
    private final LocationsConfig locationsConfig;
    private final LocationsUtil locationsUtil;

    public @NotNull LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("shop")
                .requires(this::checkBasePermission)
                .executes(this::executeBase)
                .then(Commands.argument("status", StringArgumentType.string())
                        .requires(this::checkStatusArgumentPermission)
                        .suggests(this::suggestStatusArgument)
                        .executes(this::executeStatusArgument)
                )
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
        final boolean shopEnabled = this.settingsConfig.isShopEnabled();
        if (!shopEnabled) {
            final Component shopDisabledMessageComponent = this.messagesConfig.getShopDisabledMessageComponent();
            player.sendMessage(shopDisabledMessageComponent);
            return Command.SINGLE_SUCCESS;
        }
        final Location spawnPointLocation = this.locationsConfig.getShopSpawnPointLocation();
        if (player.hasPermission("forestattack.commands.shop.bypass")) {
            player.teleport(spawnPointLocation);
            return Command.SINGLE_SUCCESS;
        }
        this.locationsUtil.teleportWithTimer(player, spawnPointLocation);
        return Command.SINGLE_SUCCESS;
    }

    private boolean checkStatusArgumentPermission(final @NotNull CommandSourceStack source) {
        return source.getSender().hasPermission("forestattack.commands.shop.status");
    }

    private CompletableFuture<Suggestions> suggestStatusArgument(final @NotNull CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        List.of("enable", "disable").forEach(builder::suggest);
        return builder.buildFuture();
    }

    private int executeStatusArgument(final @NotNull CommandContext<CommandSourceStack> context) {
        final CommandSender commandSender = context.getSource().getSender();
        final String statusName = context.getArgument("status", String.class);
        switch (statusName) {
            case "enable" -> {
                final Component shopStatusMessageComponent = this.messagesConfig.getShopStatusMessageComponent(true);
                this.settingsConfig.setShopEnabled(true);
                commandSender.sendMessage(shopStatusMessageComponent);
            }
            case "disable" -> {
                final Component shopStatusMessageComponent = this.messagesConfig.getShopStatusMessageComponent(false);
                this.settingsConfig.setShopEnabled(false);
                commandSender.sendMessage(shopStatusMessageComponent);
            }
            default -> {
                final Component usageMessageComponent = this.messagesConfig.getShopUsageMessageComponent();
                commandSender.sendMessage(usageMessageComponent);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

}

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
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.nms.INMSBinding;
import de.nicklasmatzulla.forestattack.util.LuckPermsUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@SuppressWarnings({"UnstableApiUsage", "SameReturnValue"})
public class EnderChestCommand {
    private final MessagesConfig messagesConfig;
    private final INMSBinding nmsBinding;

    public @NotNull LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("enderchest")
                .requires(this::checkBasePermission)
                .executes(this::executeBase)
                .then(Commands.argument("player", StringArgumentType.string())
                        .requires(this::checkPlayerArgumentPermission)
                        .suggests(this::suggestPlayerArgument)
                        .executes(this::executePlayerArgument)
                )
                .build();
    }

    private boolean checkBasePermission(final CommandSourceStack source) {
        return source.getSender().hasPermission("forestattack.commands.enderchest");
    }

    private int executeBase(final @NotNull CommandContext<CommandSourceStack> context) {
        final CommandSender commandSender = context.getSource().getSender();
        if (!(commandSender instanceof final Player player)) {
            final Component onlyPlayerMessageComponent = this.messagesConfig.getOnlyPlayerMessageComponent();
            commandSender.sendMessage(onlyPlayerMessageComponent);
            return Command.SINGLE_SUCCESS;
        }
        player.openInventory(player.getEnderChest());
        final Component openedSelfMessageComponent = this.messagesConfig.getEnderchestOpenedSelfMessageComponent();
        player.sendMessage(openedSelfMessageComponent);
        return Command.SINGLE_SUCCESS;
    }

    private boolean checkPlayerArgumentPermission(final @NotNull CommandSourceStack source) {
        return source.getSender().hasPermission("forestattack.commands.enderchest.other");
    }

    private CompletableFuture<Suggestions> suggestPlayerArgument(final @NotNull CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        if (context.getSource().getSender() instanceof final Player sender) {
            LuckPermsUtil.getModifiablePlayers(sender).forEach(modifiablePlayer -> builder.suggest(modifiablePlayer.getName()));
        }
        return builder.buildFuture();
    }

    private int executePlayerArgument(final @NotNull CommandContext<CommandSourceStack> context) {
        final CommandSender commandSender = context.getSource().getSender();
        if (!(commandSender instanceof final Player player)) {
            final Component onlyPlayerMessageComponent = this.messagesConfig.getOnlyPlayerMessageComponent();
            commandSender.sendMessage(onlyPlayerMessageComponent);
            return Command.SINGLE_SUCCESS;
        }
        final String victimName = context.getArgument("player", String.class);
        final OfflinePlayer victim = Bukkit.getOfflinePlayerIfCached(victimName);
        if (victim == null) {
            final Component playerNotFoundComponent = this.messagesConfig.getPlayerNotFoundMessageComponent();
            player.sendMessage(playerNotFoundComponent);
            return Command.SINGLE_SUCCESS;
        }
        if (victim.isOnline()) {
            player.openInventory(Objects.requireNonNull(victim.getPlayer()).getEnderChest());
        } else {
            final Optional<ItemStack[]> optItemStacks = this.nmsBinding.loadOfflinePlayerEnderChest("world", player.getUniqueId());
            if (optItemStacks.isEmpty()) {
                final Component failedMessageComponent = this.messagesConfig.getEnderchestFailedMessageComponent(victim);
                player.sendMessage(failedMessageComponent);
                return Command.SINGLE_SUCCESS;
            }
            final ItemStack[] itemStacks = optItemStacks.get();
            final Component titleComponent = this.messagesConfig.getEnderchestOfflinePlayerTitleComponent(victim);
            final Gui gui = Gui.gui()
                    .title(titleComponent)
                    .rows(3)
                    .disableAllInteractions()
                    .create();
            for (int i = 0; i < itemStacks.length; i++) {
                final ItemStack itemStack = itemStacks[i];
                if (itemStack != null) {
                    gui.setItem(i, ItemBuilder.from(itemStack).asGuiItem(event -> {
                        final Component unsupportedOperationMessageComponent = this.messagesConfig.getUnsupportedOperationMessageComponent();
                        player.sendMessage(unsupportedOperationMessageComponent);
                    }));
                }
            }
            gui.open(player);
        }
        final Component openedOtherMessageComponent = this.messagesConfig.getEnderchestOpenedOtherMessageComponent(victim);
        player.sendMessage(openedOtherMessageComponent);
        return Command.SINGLE_SUCCESS;
    }

}

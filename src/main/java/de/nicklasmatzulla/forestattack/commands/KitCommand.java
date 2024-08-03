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
import de.nicklasmatzulla.forestattack.api.profile.IProfile;
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.config.SettingsConfig;
import de.nicklasmatzulla.forestattack.profile.ProfileManagerImpl;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
@AllArgsConstructor
public class KitCommand {
    private final Logger logger;
    private final SettingsConfig settingsConfig;
    private final MessagesConfig messagesConfig;
    private final ProfileManagerImpl profileManager;

        public @NotNull LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("kit")
                .requires(this::checkBasePermission)
                .executes(this::executeBase)
                .build();
    }

    private boolean checkBasePermission(final CommandSourceStack source) {
            return true;
    }

    @SuppressWarnings({"DuplicatedCode", "SameReturnValue"})
    @SneakyThrows
    private int executeBase(final @NotNull CommandContext<CommandSourceStack> context) {
            final CommandSender commandSender = context.getSource().getSender();
            if (!(commandSender instanceof final Player player)) {
                final Component onlyPlayerMessageComponent = this.messagesConfig.getOnlyPlayerMessageComponent();
                commandSender.sendMessage(onlyPlayerMessageComponent);
                return Command.SINGLE_SUCCESS;
            }
            final UUID uniqueId = player.getUniqueId();
            final IProfile profile = this.profileManager.getOrLoad(uniqueId).orElseThrow();
            if (profile.hasStarterKitClaimed()) {
                final Component kitAlreadyClaimedMessageComponent = this.messagesConfig.getKitAlreadyClaimedMessageComponent();
                player.sendMessage(kitAlreadyClaimedMessageComponent);
                return Command.SINGLE_SUCCESS;
            }
            try {
                profile.setStarterKitClaimed(true);
                this.settingsConfig.getStarterKitItemStacks().forEach(itemStack -> player.getInventory().addItem(itemStack));
                final Component kitClaimedMessageComponent = this.messagesConfig.getKitClaimedMessageComponent();
                player.sendMessage(kitClaimedMessageComponent);
            } catch (final @NotNull SQLException e) {
                this.logger.error("Cannot save player data", e);
                final Component errorLoadDataKickComponent = this.messagesConfig.getErrorLoadDataKickComponent();
                final Component kickMessageComponent = this.messagesConfig.getKickMessageComponent(errorLoadDataKickComponent);
                player.kick(kickMessageComponent);
            }
            return Command.SINGLE_SUCCESS;
    }

}

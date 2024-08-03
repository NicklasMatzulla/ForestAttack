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

package de.nicklasmatzulla.forestattack.profile.listener;

import de.nicklasmatzulla.forestattack.api.profile.IProfile;
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.profile.ProfileManagerImpl;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@AllArgsConstructor
public class ProfilePlayerJoinListener implements Listener {
    private final Logger logger;
    private final ProfileManagerImpl profileManager;
    private final MessagesConfig messagesConfig;

    @EventHandler
    public void onPlayerJoinEvent(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        try {
            final UUID uniqueId = player.getUniqueId();
            final Optional<IProfile> optProfile = this.profileManager.getOrLoad(uniqueId);
            if (optProfile.isEmpty()) {
                this.profileManager.create(uniqueId);
            }
        } catch (final @NotNull SQLException e) {
            this.logger.error("Failed to load player data", e);
            final Component errorLoadDataKickComponent = this.messagesConfig.getErrorLoadDataKickComponent();
            final Component kickMessageComponent = this.messagesConfig.getKickMessageComponent(errorLoadDataKickComponent);
            player.kick(kickMessageComponent);
        }
    }

}

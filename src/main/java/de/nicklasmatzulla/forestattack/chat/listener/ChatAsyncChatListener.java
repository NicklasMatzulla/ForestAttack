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

package de.nicklasmatzulla.forestattack.chat.listener;

import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.util.LuckPermsUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@AllArgsConstructor
public class ChatAsyncChatListener implements Listener {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final MessagesConfig messagesConfig;

    @EventHandler
    public void onAsyncChatEvent(final @NotNull AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final Component originalMessage = event.message();
        final String chatFormat = this.messagesConfig.getChatFormat();
        final Component prefixedAndColoredPlayerName = LuckPermsUtil.getPrefixedAndColoredPlayerName(player);
        event.renderer(((source, sourceDisplayName, message, viewer) -> MINI_MESSAGE.deserialize(chatFormat)
                .replaceText(builder -> builder.match("<name_prefixed_and_colored>").replacement(prefixedAndColoredPlayerName))
                .replaceText(builder -> builder.match("<message>").replacement(originalMessage))
        ));
    }

}

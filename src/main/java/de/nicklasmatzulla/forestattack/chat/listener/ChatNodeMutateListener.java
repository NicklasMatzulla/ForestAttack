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

import de.nicklasmatzulla.forestattack.chat.TabListStyle;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ChatNodeMutateListener {
    private final TabListStyle tabListStyle;

    @SuppressWarnings("resource")
    public ChatNodeMutateListener(final @NotNull Plugin plugin, final @NotNull TabListStyle tabListStyle) {
        this.tabListStyle = tabListStyle;
        final LuckPerms luckPerms = LuckPermsProvider.get();
        final EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(plugin, NodeMutateEvent.class, this::onNodeMutateEvent);
    }

    public void onNodeMutateEvent(@NotNull final NodeMutateEvent event) {
        if (!event.isUser()) {
            return;
        }
        final User user = (User) event.getTarget();
        final Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player == null) {
            return;
        }
        this.tabListStyle.refreshPlayer(player);
    }

}

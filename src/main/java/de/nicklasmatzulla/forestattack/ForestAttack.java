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

package de.nicklasmatzulla.forestattack;

import com.zaxxer.hikari.HikariDataSource;
import de.nicklasmatzulla.commons.db.DatabaseManagerImpl;
import de.nicklasmatzulla.forestattack.chat.TabListStyle;
import de.nicklasmatzulla.forestattack.chat.listener.*;
import de.nicklasmatzulla.forestattack.commands.*;
import de.nicklasmatzulla.forestattack.config.ConfidentialConfig;
import de.nicklasmatzulla.forestattack.config.LocationsConfig;
import de.nicklasmatzulla.forestattack.config.MessagesConfig;
import de.nicklasmatzulla.forestattack.config.SettingsConfig;
import de.nicklasmatzulla.forestattack.hooks.PlaceholderAPIHook;
import de.nicklasmatzulla.forestattack.nms.v1_21_R1.NMSBindingImpl_v1_20_R1;
import de.nicklasmatzulla.forestattack.profile.ProfileManagerImpl;
import de.nicklasmatzulla.forestattack.profile.listener.ProfilePlayerJoinListener;
import de.nicklasmatzulla.forestattack.protection.listener.*;
import de.nicklasmatzulla.forestattack.util.LocationsUtil;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

@SuppressWarnings("unused")
public class ForestAttack extends JavaPlugin {

    private HikariDataSource dataSource;

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onEnable() {
        final Logger logger = getSLF4JLogger();
        try {
            final PluginManager pluginManager = Bukkit.getPluginManager();

            // Configurations
            final ConfidentialConfig confidentialConfig = new ConfidentialConfig(logger);
            final SettingsConfig settingsConfig = new SettingsConfig(logger);
            final MessagesConfig messagesConfig = new MessagesConfig(logger);
            final LocationsConfig locationsConfig = new LocationsConfig(logger);

            // Establish database connection
            this.dataSource = confidentialConfig.createHikariDataSource();
            final DatabaseManagerImpl databaseManager = new DatabaseManagerImpl(dataSource);

            // Managers
            final ProfileManagerImpl profileManager = new ProfileManagerImpl(logger, databaseManager);

            // Registration of utility and helper classes
            final TabListStyle tabListStyle = new TabListStyle(this, messagesConfig);
            final LocationsUtil locationsUtil = new LocationsUtil(this, locationsConfig);
            final NMSBindingImpl_v1_20_R1 nmsBinding = new NMSBindingImpl_v1_20_R1();

            // Listener registration
            // Chat listeners
            pluginManager.registerEvents(new ChatAsyncChatListener(messagesConfig), this);
            new ChatNodeMutateListener(this, tabListStyle);
            pluginManager.registerEvents(new ChatPlayerShowListener(tabListStyle), this);
            pluginManager.registerEvents(new ChatPlayerJoinListener(this, messagesConfig, tabListStyle), this);
            pluginManager.registerEvents(new ChatPlayerQuitListener(messagesConfig, tabListStyle), this);
            pluginManager.registerEvents(new ChatPlayerHideListener(tabListStyle), this);
            // Protection listeners
            pluginManager.registerEvents(new ProtectionBlockBreakListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionBlockExplodeListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionBlockPistonRetractListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionBlockPlaceListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionCreatureSpawnListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionEntityDamageListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionEntityExplodeListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionPistonExtendListener(locationsUtil), this);
            pluginManager.registerEvents(new ProtectionPlayerInteractListener(locationsUtil), this);
            // Profile listeners
            pluginManager.registerEvents(new ProfilePlayerJoinListener(logger, profileManager, messagesConfig), this);

            // Register commands
            final @NotNull LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
            manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                final Commands commands = event.registrar();
                commands.register(new BiomeCommand(messagesConfig).createBrigadierCommand(),
                        "Displays the name of the biome in which the player is located.",
                        List.of("biom")
                );
                commands.register(new ChunkBorderCommand(this, messagesConfig).createBrigadierCommand(),
                        "Makes the chunk boundaries visible",
                        List.of("cb")
                );
                commands.register(new EnderChestCommand(messagesConfig, nmsBinding).createBrigadierCommand(),
                        "Opens the own Enderchest or that of another player.",
                        List.of("ec")
                );
                commands.register(new KitCommand(logger, settingsConfig, messagesConfig, profileManager).createBrigadierCommand(),
                        "Receive the starter kit.",
                        List.of("starterkit", "startkit")
                );
                commands.register(new ShopCommand(settingsConfig, messagesConfig, locationsConfig, locationsUtil).createBrigadierCommand(),
                        "Teleports the player to the shop location."
                );
                commands.register(new SpawnCommand(messagesConfig, locationsConfig, locationsUtil).createBrigadierCommand(),
                        "Teleports the player to the spawn point."
                );
            });

            // Hooks registration
            new PlaceholderAPIHook().register();
        } catch (final @NotNull Exception e) {
            logger.error("Failed to register plugin functions, shutting down server", e);
            Bukkit.getServer().shutdown();
        }
    }

    @Override
    public void onDisable() {
        this.dataSource.close();
    }
}

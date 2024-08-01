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

import de.nicklasmatzulla.forestattack.config.LocationsConfig;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class LocationsUtil {
    private final Plugin plugin;
    private final LocationsConfig locationsConfig;

    public boolean inProtectedArea(final @NotNull Location location) {
        final Location spawnMinLocation = this.locationsConfig.getSpawnProtectionMinLocation();
        final Location spawnMaxLocation = this.locationsConfig.getSpawnProtectionMaxLocation();
        final Location shopMinLocation = this.locationsConfig.getShopProtectionMinLocation();
        final Location shopMaxLocation = this.locationsConfig.getShopProtectionMaxLocation();
        return isInArea(location, spawnMinLocation, spawnMaxLocation) || isInArea(location, shopMinLocation, shopMaxLocation);
    }

    public boolean isInArea(final @NotNull Location location, final @NotNull Location min, final @NotNull Location max) {
        if (!location.getWorld().equals(min.getWorld()) || !location.getWorld().equals(max.getWorld())) {
            return false;
        }

        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();

        final double minX = Math.min(min.getX(), max.getX());
        final double maxX = Math.max(min.getX(), max.getX());
        final double minY = Math.min(min.getY(), max.getY());
        final double maxY = Math.max(min.getY(), max.getY());
        final double minZ = Math.min(min.getZ(), max.getZ());
        final double maxZ = Math.max(min.getZ(), max.getZ());

        return (x >= minX && x <= maxX) &&
               (y >= minY && y <= maxY) &&
               (z >= minZ && z <= maxZ);
    }

    public void teleportWithTimer(final @NotNull Player player, final @NotNull Location location) {
        final Location startLocation = player.getLocation();
        final AtomicInteger secondsPassed = new AtomicInteger(0);
        // Create a new task to handle the subtitle updates and teleportation
        BukkitTask subtitleTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getLocation().distance(startLocation) > 0) {
                    cancel();
                    return;
                }

                int elapsedTime = secondsPassed.incrementAndGet();
                Component progress = getProgressBar(elapsedTime);
                player.showTitle(Title.title(Component.empty(), progress, Title.Times.times(Duration.ofMillis(0), Duration.ofMillis(1500), Duration.ofMillis(1500))));

                if (elapsedTime >= 5) {
                    player.teleport(location);
                    cancel();
                }
            }
        }.runTaskTimer(this.plugin, 0, 20); // Run every second

        // Optionally, cancel the task if the player logs out or the plugin is disabled
        Bukkit.getScheduler().runTaskLater(this.plugin, subtitleTask::cancel, 20 * 60 * 5); // Cancel after 5 minutes
    }

    private @NotNull Component getProgressBar(int secondsElapsed) {
        Component[] colors = {
                Component.text("■").color(TextColor.color(128, 128, 128)), // Gray
                Component.text("■").color(TextColor.color(96, 96, 96)),  // Dark Gray
                Component.text("■").color(TextColor.color(64, 64, 64)),  // Darker Gray
                Component.text("■").color(TextColor.color(48, 48, 48)),  // Even Darker Gray
                Component.text("■").color(TextColor.color(32, 32, 32)),   // Darkest Gray
                Component.text("■").color(NamedTextColor.GRAY)
        };

        TextComponent.Builder progressBar = Component.text();
        for (int i = 0; i < 5; i++) {
            progressBar.append(secondsElapsed > i ? colors[i] : colors[5]);
        }

        return progressBar.build();
    }

}

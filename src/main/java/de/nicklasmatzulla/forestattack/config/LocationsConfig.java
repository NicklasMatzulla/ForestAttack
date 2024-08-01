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
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

@Getter
public class LocationsConfig extends BaseConfig {
    private final Location spawnSpawnPointLocation;
    private final Location spawnProtectionMinLocation;
    private final Location spawnProtectionMaxLocation;
    private final Location shopSpawnPointLocation;
    private final Location shopProtectionMinLocation;
    private final Location shopProtectionMaxLocation;

    public LocationsConfig(final @NotNull Logger logger) {
        super(logger, new File("plugins/ForestAttack/locations.yml"), "config/locations.yml", true);
        this.spawnSpawnPointLocation = getLocation("spawn.spawnPosition");
        this.spawnProtectionMinLocation = getLocation("spawn.protection.min");
        this.spawnProtectionMaxLocation = getLocation("spawn.protection.max");
        this.shopSpawnPointLocation = getLocation("shop.spawnPosition");
        this.shopProtectionMinLocation = getLocation("shop.protection.min");
        this.shopProtectionMaxLocation = getLocation("shop.protection.max");
    }
}
